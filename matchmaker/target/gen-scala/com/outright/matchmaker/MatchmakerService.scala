package com.outright.matchmaker

// Autogenerated

import java.net.InetSocketAddress
import java.util.{List => JList, Map => JMap, Set => JSet}
import scala.collection._
import scala.collection.JavaConversions._
import org.apache.thrift.protocol._

import com.twitter.conversions.time._
import com.twitter.finagle.builder._
import com.twitter.finagle.stats._
import com.twitter.finagle.thrift._
import com.twitter.logging.Logger
import com.twitter.ostrich.admin.Service
import com.twitter.util._

trait MatchmakerService {
  implicit def voidUnit(f: Future[_]): Future[java.lang.Void] = f.map(x=>null)

  
    def getmatchingelements(fromstring: String, tostring: String, timespan: Int): Future[String]
  
    def echocheck(echostring: String): Future[String]
  

  def toThrift = new MatchmakerServiceThriftAdapter(this)
}

trait MatchmakerServiceServer extends Service with MatchmakerService{
  val log = Logger.get(getClass)

  def thriftCodec = ThriftServerFramedCodec()
  val thriftProtocolFactory = new TBinaryProtocol.Factory()
  val thriftPort: Int
  val serverName: String

  var server: Server = null

  def start = {
    val thriftImpl = new com.outright.matchmaker.thrift.MatchmakerService.Service(toThrift, thriftProtocolFactory)
    val serverAddr = new InetSocketAddress(thriftPort)
    server = ServerBuilder().codec(thriftCodec).name(serverName).reportTo(new OstrichStatsReceiver).bindTo(serverAddr).build(thriftImpl)
  }

  def shutdown = synchronized {
    if (server != null) {
      server.close(0.seconds)
    }
  }
}

class MatchmakerServiceThriftAdapter(val matchmakerService: MatchmakerService) extends com.outright.matchmaker.thrift.MatchmakerService.ServiceIface {
  val log = Logger.get(getClass)
  def this() = this(null)

  
    def getmatchingelements(fromstring: String, tostring: String, timespan: Int) = {
      matchmakerService.getmatchingelements(fromstring, tostring, timespan.intValue)
      
        .map[String] { retval =>
          retval
        }
      
      
    }
  
    def echocheck(echostring: String) = {
      matchmakerService.echocheck(echostring)
      
        .map[String] { retval =>
          retval
        }
      
      
    }
  
}

class MatchmakerServiceClientAdapter(val matchmakerService: com.outright.matchmaker.thrift.MatchmakerService.ServiceIface) extends MatchmakerService {
  val log = Logger.get(getClass)
  def this() = this(null)

  
    def getmatchingelements(fromstring: String, tostring: String, timespan: Int) = {
      matchmakerService.getmatchingelements(fromstring, tostring, timespan)
      
        .map[String] { retval =>
          retval
        }
      
      
    }
  
    def echocheck(echostring: String) = {
      matchmakerService.echocheck(echostring)
      
        .map[String] { retval =>
          retval
        }
      
      
    }
  
}