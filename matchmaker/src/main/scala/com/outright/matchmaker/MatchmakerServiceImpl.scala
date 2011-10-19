package com.outright.matchmaker

import java.util.concurrent.Executors
import scala.collection.mutable
import com.twitter.util._
import config._

import com.twitter.ostrich.stats.Stats

class MatchmakerServiceImpl(config: MatchmakerServiceConfig) extends MatchmakerServiceServer {

  val serverName = "Matchmaker"
  val thriftPort = config.thriftPort

  /**
   * These services are based on finagle, which implements a nonblocking server.  If you
   * are making blocking rpc calls, it's really important that you run these actions in
   * a thread pool, so that you don't block the main event loop.  This thread pool is only
   * needed for these blocking actions.  The code looks like:
  
   *     val futurePool = new FuturePool(Executors.newFixedThreadPool(config.threadPoolSize))
   *
   *     def hello() = futurePool {
   *       someService.blockingRpcCall
   *     }
   *
   */

  def echocheck(echostring : String ) = Future(echostring)

  def getmatchingelements(fromstring : String, tostring : String, timespan : Int) = 
  {
	 Stats.incr("matchmaker.performatching")
	 Stats.time("matchmaker.put.latency") {
	 	Future(MatchMaker.performMatching(fromstring, tostring, timespan))
	}
  }


}
