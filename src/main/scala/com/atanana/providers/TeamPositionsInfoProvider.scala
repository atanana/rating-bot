package com.atanana.providers

import com.atanana.Connector
import com.atanana.data.{Team, TeamPositionsInfo}
import com.atanana.parsers.TeamsPageParser
import javax.inject.Inject

class TeamPositionsInfoProvider @Inject()(
                                           connector: Connector,
                                           parser: TeamsPageParser,
                                           composer: TeamPositionsInfoComposer
                                         ) {

  def data: Either[String, TeamPositionsInfo] = for {
    allTeams <- connector.getTeamsPage.map(parser.getTeams)
    cityTeams <- connector.getCityTeamsPage.map(parser.getTeams)
    countryTeams <- connector.getCountryTeamsPage.map(parser.getTeams)

    positionsInfo <- composer.positionsInfo(
      teams = filter(allTeams),
      cityTeams = filter(cityTeams),
      countryTeams = filter(countryTeams)
    )
  } yield positionsInfo

  private def filter(teams: List[Team]): List[Team] = teams.filter(_.isReal)
}
