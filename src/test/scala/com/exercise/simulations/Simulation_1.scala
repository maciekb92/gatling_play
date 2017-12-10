package com.exercise.simulations

import com.exercise.elements.{Constants, Play}
import io.gatling.core.Predef.atOnceUsers
import io.gatling.core.Predef._

class Simulation_1 extends Simulation {

  setUp(
    Play.scn.inject(atOnceUsers(Constants.numberOfUsers))
  )
    .protocols(Constants.httpConf)
    .maxDuration(Constants.duration)
}