import sbt._
import Process._
import com.twitter.sbt._

/**
 * Sbt project files are written in a DSL in scala.
 *
 * The % operator is just turning strings into maven dependency declarations, so lines like
 *     val example = "com.example" % "exampleland" % "1.0.3"
 * mean to add a dependency on exampleland version 1.0.3 from provider "com.example".
 */
class MatchmakerProject(info: ProjectInfo) extends StandardServiceProject(info)
  with CompileThriftScala
  with NoisyDependencies
  with DefaultRepos
  with SubversionPublisher
  with PublishSourcesAndJavadocs
  with PublishSite
{
  val finagleVersion = "1.2.5"

  val finagleC = "com.twitter" % "finagle-core" % finagleVersion
  val finagleT = "com.twitter" % "finagle-thrift" % finagleVersion
  val finagleO = "com.twitter" % "finagle-ostrich4" % finagleVersion

  // thrift
  val libthrift = "thrift" % "libthrift" % "0.5.0"
  val util = "com.twitter" % "util" % "1.8.3"

  override def originalThriftNamespaces = Map("Matchmaker" -> "com.outright.matchmaker.thrift")
  override val scalaThriftTargetNamespace = "com.outright.matchmaker"

  val slf4jVersion = "1.5.11"
  val slf4jApi = "org.slf4j" % "slf4j-api" % slf4jVersion withSources() intransitive()
  val slf4jBindings = "org.slf4j" % "slf4j-jdk14" % slf4jVersion withSources() intransitive()

  // for tests
  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7" % "test" withSources()
  val jmock = "org.jmock" % "jmock" % "2.4.0" % "test"
  val hamcrest_all = "org.hamcrest" % "hamcrest-all" % "1.1" % "test"
  val cglib = "cglib" % "cglib" % "2.1_3" % "test"
  val asm = "asm" % "asm" % "1.5.3" % "test"
  val objenesis = "org.objenesis" % "objenesis" % "1.1" % "test"

  val specsVersion = crossScalaVersionString match {
      case "2.8.0" => "1.6.5"
      case _ => "1.6.6"
    }

  val liftwebjson = "net.liftweb" % "lift-json_2.8.1" % "2.4-M4" % "compile"
  val jodatime = "joda-time" % "joda-time" % "2.0" % "compile"
  val jodaconvert = "org.joda" % "joda-convert" % "1.1" % "compile"
  val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"
  // val specs = "org.scala-tools.testing" % ("specs_" + crossScalaVersionString) % specsVersion % "test"
  val paranamer  = "com.thoughtworks.paranamer" % "paranamer" % "2.0" % "compile->default"
  val junit      = "junit" % "junit" % "4.5" % "test"
  val scalacheck = "org.scala-tools.testing" %% "scalacheck" % "1.7" % "test"

  override def compileOptions = super.compileOptions ++ compileOptions("-unchecked")

  override def mainClass = Some("com.outright.matchmaker.Main")

  override def subversionRepository = Some("http://svn.local.twitter.com/maven")
}
