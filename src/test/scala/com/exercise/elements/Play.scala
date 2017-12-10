package com.exercise.elements

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck

object Play {

  val scn = scenario("Play")
      .exec(http("Authorization")
      .get("/game.web/service")
      .queryParam("fn", "authenticate")
      .queryParam("org", "Demo")
      .queryParam("gameid", "7316")
      .check(jsonPath("$.data.sessid").saveAs("sessId"))
    ).pause(Constants.pause)

    .exec(session => {

      val getCmd = Constants.checkCmd

      do {
        http("Play")
          .get("/game.web/service")
          .queryParam("fn", "play")
          .queryParam("org", "Demo")
          .queryParam("gameid", "7316")
          .queryParam("sessid", "${session(\"sessId\")}")
          .queryParam("currency", "EUR")
          .queryParam("amount", "0.25")
          .queryParam("coin", "0.01")
          getCmd
        //.check(jsonPath("$.data.wager.bets[0].betdata.cmd").optional.saveAs("cmd"))
      } while(getCmd.toString != "C")

      val cmd = session("cmd").as[String]

      val checkWonAmount: HttpCheck = session("cmd").asOption[String] match {
        case Some("C") => jsonPath("$.data.wager.bets[-1].wonamount").ofType[Double].saveAs("wonAmount")
        case _ => jsonPath("")
      }

      //val wonAmount = session("wonAmount").as[Double]

      session
    })
}
