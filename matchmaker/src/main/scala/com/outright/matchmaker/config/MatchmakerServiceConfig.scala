package com.outright.matchmaker
package config

import com.twitter.logging.Logger
import com.twitter.logging.config._
import com.twitter.ostrich.admin.{RuntimeEnvironment, ServiceTracker}
import com.twitter.ostrich.admin.config._
import com.twitter.util.Config

class MatchmakerServiceConfig extends ServerConfig[MatchmakerServiceServer] {
  var thriftPort: Int = 9999

  def apply(runtime: RuntimeEnvironment) = new MatchmakerServiceImpl(this)
}
