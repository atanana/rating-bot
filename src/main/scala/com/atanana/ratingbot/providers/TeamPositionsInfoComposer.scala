package com.atanana.ratingbot.providers

import com.atanana.ratingbot.data.{Team, TeamPositionsInfo}

trait TeamPositionsInfoComposer {

  def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Either[String, TeamPositionsInfo]
}
