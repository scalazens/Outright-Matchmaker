namespace java com.outright.matchmaker.thrift
namespace rb Matchmaker

/**
 * It's considered good form to declare an exception type for your service.
 * Thrift will serialize and transmit them transparently.
 */
exception MatchmakerException {
  1: string description
}

/**
 * A simple memcache-like service, which stores strings by key/value.
 * You should replace this with your actual service.
 */
service MatchmakerService {
  string echocheck(1 : string echostring)
  string getmatchingelements(1: string fromstring, 2: string tostring, 3: i32 timespan)
}
