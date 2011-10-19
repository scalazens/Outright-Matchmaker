package com.outright.matchmaker

import org.specs.Specification
import com.twitter.ostrich.admin._
import com.twitter.util._
import com.twitter.conversions.time._

abstract class AbstractSpec extends Specification {
  val env = RuntimeEnvironment(this, Array("-f", "config/test.scala"))
  lazy val matchmaker = {
    val out = env.loadRuntimeConfig[MatchmakerService]

    // You don't really want the thrift server active, particularly if you
    // are running repetitively via ~test
    ServiceTracker.shutdown // all services
    out
  }
}
