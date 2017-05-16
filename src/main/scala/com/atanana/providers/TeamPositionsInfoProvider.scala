package com.atanana.providers

import javax.inject.Inject

import com.atanana.Connector
import com.atanana.data.TeamPositionsInfo
import com.atanana.parsers.TeamsPageParser

import scala.util.Try

class TeamPositionsInfoProvider @Inject()(connector: Connector,
                                          parser: TeamsPageParser,
                                          composer: TeamPositionsInfoComposer) {
  def data: Try[TeamPositionsInfo] = {
    val allTeams = parser.getTeams(connector.getTeamsPage)
    val cityTeams = parser.getTeams(connector.getCityTeamsPage)
    val countryTeams = parser.getTeams(connector.getCountryTeamsPage)

    composer.positionsInfo(allTeams, cityTeams, countryTeams)
  }
}
