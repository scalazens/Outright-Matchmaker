package com.outright.matchmaker

import com.twitter.ostrich.admin.RuntimeEnvironment

object Main {
  def main(args: Array[String]) {
    val env = RuntimeEnvironment(this, args)
    val service = env.loadRuntimeConfig[MatchmakerServiceServer]
    service.start()
  }
}

