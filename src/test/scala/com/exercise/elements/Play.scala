package com.exercise.elements

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Play {

  val scn = scenario("Play_Request")
    .exec(http("Authorization")
      .get("/game.web/service")
      .queryParam("fn", "authenticate")
      .queryParam("org", "Demo")
      .queryParam("gameid", "7316")
      .check(jsonPath("$.data.sessid").saveAs("sessId"))
    ).pause(Constants.pause)
    .asLongAs( session => session("wonAmount").validate[String].map(wonAmount => !wonAmount.contains("0.00")).recover(true))
    {
      exec(http("Play")
        .get("/game.web/service")
        .queryParam("fn", "play")
        .queryParam("org", "Demo")
        .queryParam("gameid", "7316")
        .queryParam("sessid", "${sessId}")
        .queryParam("currency", "EUR")
        .queryParam("amount", "0.25")
        .queryParam("coin", "0.01")
        .check(jsonPath("$.data.wager.bets[0].eventdata.accWa").saveAs("accWa"))
        .check(jsonPath("$.data.wager.wagerid").saveAs("wagerId"))
      ).pause(Constants.pause)
        .doIf(session => session("accWa").as[String] != "0.00")
        {
          exec(http("Collect")
            .get("/game.web/service")
            .queryParam("fn", "play")
            .queryParam("gameid", "7316")
            .queryParam("sessid", "${sessId}")
            .queryParam("currency", "EUR")
            .queryParam("amount", "0")
            .queryParam("wagerid", "${wagerId}")
            .queryParam("betid", "1")
            .queryParam("step", "2")
            .queryParam("cmd", "C")
            .check(jsonPath("$.data.wager.bets[0].wonamount").findAll.saveAs("wonAmount"))
          ).pause(Constants.pause)
        }
    }
}