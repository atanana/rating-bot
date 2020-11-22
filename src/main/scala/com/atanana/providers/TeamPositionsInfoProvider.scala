package com.atanana.providers

import com.atanana.Connector
import com.atanana.data.{Team, TeamPositionsInfo}
import com.atanana.parsers.TeamsPageParser
import javax.inject.Inject

import scala.util.Try

class TeamPositionsInfoProvider @Inject()(connector: Connector,
                                          parser: TeamsPageParser,
                                          composer: TeamPositionsInfoComposer) {
  def data: Either[String, TeamPositionsInfo] = {
    val allTeams = parser.getTeams(connector.getTeamsPage.right.get)
    val cityTeams = parser.getTeams(connector.getCityTeamsPage)
    val countryTeams = parser.getTeams(connector.getCountryTeamsPage)

    composer.positionsInfo(
      filter(allTeams),
      filter(cityTeams),
      filter(countryTeams)
    )
  }

  private def filter(teams: List[Team]): List[Team] = teams.filter(_.isReal)
}
