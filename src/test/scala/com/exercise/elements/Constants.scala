package com.exercise.elements

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

object Constants {

  val numberOfUsers: Int = System.getProperty("numberOfUsers").toInt
  val duration: FiniteDuration = System.getProperty("durationMinutes").toInt.minutes
  val pause: FiniteDuration = System.getProperty("pauseBetweenRequestsMs").toInt.millisecond
  private val url: String = System.getProperty("url")
  private val successStatus: Int = 200

  val httpConf = http
    .baseURL(url)
    .check(status.is(successStatus))
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
}