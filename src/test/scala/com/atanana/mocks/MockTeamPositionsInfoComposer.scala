package com.atanana.mocks

import com.atanana.data.{Team, TeamPositionsInfo}
import com.atanana.providers.TeamPositionsInfoComposer

import scala.collection.mutable

class MockTeamPositionsInfoComposer extends TeamPositionsInfoComposer {

  val responses: mutable.Map[(List[Team], List[Team], List[Team]), Either[String, TeamPositionsInfo]] = mutable.Map()

  override def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Either[String, TeamPositionsInfo] =
    responses((teams, cityTeams, countryTeams))
}
