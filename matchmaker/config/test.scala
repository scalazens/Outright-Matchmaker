import com.twitter.conversions.time._
import com.twitter.logging.config._
import com.twitter.ostrich.admin.config._
import com.outright.matchmaker.config._

// test mode.
new MatchmakerServiceConfig {

  // Add your own config here

  // Where your service will be exposed.
  thriftPort = 9999

  // Ostrich http admin port.  Curl this for stats, etc
  admin.httpPort = 9900

  // End user configuration

  // Expert-only: Ostrich stats and logger configuration.

  loggers =
    new LoggerConfig {
      level = Level.INFO
      handlers = new ConsoleHandlerConfig
    }
}
