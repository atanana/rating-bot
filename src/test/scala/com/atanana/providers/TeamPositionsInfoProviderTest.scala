package com.atanana.providers

import com.atanana.Connector
import com.atanana.data.{Team, TeamPositionsInfo}
import com.atanana.parsers.TeamsPageParser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.Success

class TeamPositionsInfoProviderTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  var connector: Connector = _
  var parser: TeamsPageParser = _
  var composer: TeamPositionsInfoComposer = _
  var provider: TeamPositionsInfoProvider = _

  before {
    connector = stub[Connector]
    parser = stub[TeamsPageParser]
    composer = stub[TeamPositionsInfoComposer]
    provider = new TeamPositionsInfoProvider(connector, parser, composer)
  }

  "TeamPositionsInfoProvider" should {
    "provide correct info" in {
      (connector.getTeamsPage _).when().returns("teams page")
      (connector.getCityTeamsPage _).when().returns("city teams page")
      (connector.getCountryTeamsPage _).when().returns("country teams page")

      val team = mock[Team]
      val cityTeam = mock[Team]
      val countryTeam = mock[Team]
      (parser.getTeams _).when("teams page").returns(List(team))
      (parser.getTeams _).when("city teams page").returns(List(cityTeam))
      (parser.getTeams _).when("country teams page").returns(List(countryTeam))

      (composer.positionsInfo _).when(List(team), List(cityTeam), List(countryTeam)).returns(
        Success(TeamPositionsInfo("test team", "test city", 123, 200, 3000, 20, 30))
      )

      provider.data shouldEqual Success(TeamPositionsInfo("test team", "test city", 123, 200, 3000, 20, 30))
    }
  }
}
