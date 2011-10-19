package com.twitter.sbt

import java.util.Properties
import java.io.{FileOutputStream, FileInputStream}

import _root_.sbt.{BasicManagedProject, BasicDependencyProject, Version}
import _root_.sbt.Process._
import pimpedversion._

trait ReleaseManagement extends BasicManagedProject with GitHelpers {
  private def versionToString(version: Version): String =
    versionToString(version.toString)
  private def versionToString(version: String): String =
    "org=%s,name=%s,version=%s".format(organization, name, version)

  private val releasePropertiesPath = info.projectPath / "project" / "release.properties"

  def prepareForReleaseTask = task {
    val versionString = versionToString(projectVersion.value)

    val tags = ("git tag -l | grep " + versionString) !!

    if (!gitIsCleanWorkingTree) {
      Some("Cannot publish release. Working directory is not clean.")
    } else if (libraryDependencies.exists(_.revision.contains("SNAPSHOT"))) {
      Some("Cannot publish a release with snapshotted dependencies.")
    } else if (tags.contains(versionString) && !tags.contains("SNAPSHOT")) {
      Some("Cannot publish release version '" +
           versionString +
           "'. Tag for that release already exists.")
    } else {
      stripSnapshotExtraTask.run
    }
  }

  lazy val prepareForRelease = prepareForReleaseTask

  private def stripSnapshotExtraTask = task {
    projectVersion.update(projectVersion.value.stripSnapshot())
    saveEnvironment()

    None
  }

  def finalizeReleaseTask = task {
    val version = projectVersion.value
    val newVersion = projectVersion.value.incMicro().addSnapshot()

    // commit and tag the release
    gitCommitSavedEnvironment(Some(versionToString(version)))

    val headRefs = "git show-ref refs/heads/master" lines_!

    require(headRefs.size == 1)
    val Array(sha1, _) = headRefs(0).split(" ")

    gitTag(versionToString(version))

    // reset version to the new working version
    projectVersion.update(newVersion)
    saveEnvironment()
    // Save a properties file with our version number in it.

    val prop = new Properties
    prop.setProperty("version", version.toString)
    prop.setProperty("sha1", sha1)
    prop.store(
      new FileOutputStream(releasePropertiesPath.toString),
      "Automatically generated by ReleaseManagement")

    gitCommitSavedEnvironment(Some(versionToString(newVersion)))

    None
  }

  lazy val finalizeRelease = finalizeReleaseTask

  def publishReleaseTask = interactiveTask {
    // publishing local is required first with subprojects for hard to understand reasons
    val cmd = if (!subProjects.isEmpty) {
      "sbt +publish-local +publish"
    } else {
      "sbt +publish"
    }
    val exitCode = (cmd !)
    if (exitCode == 0) None
    else Some(cmd + " exit code " + exitCode)
  }

  val PublishReleaseDescription = "Publish a release to maven. commits and tags version in git."

  lazy val publishRelease = {
    (info.parent match {
      case Some(_: ReleaseManagement) =>
        // skip, let parent do the work
        task { None }

      case _ =>
        task { log.info("Publishing new release: " + projectVersion.value.stripSnapshot()); None } &&
        prepareForReleaseTask &&
        publishReleaseTask &&
        finalizeReleaseTask &&
        task { log.info("Don't forget to push the version change to origin"); None }
    }) describedAs PublishReleaseDescription
  }
}