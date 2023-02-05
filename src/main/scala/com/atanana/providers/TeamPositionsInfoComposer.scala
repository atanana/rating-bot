package com.atanana.providers

import com.atanana.data.{Team, TeamPositionsInfo}

trait TeamPositionsInfoComposer {

  def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Either[String, TeamPositionsInfo]
}
