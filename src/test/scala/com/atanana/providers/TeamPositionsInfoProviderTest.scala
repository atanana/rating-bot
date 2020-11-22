package com.atanana.providers

import com.atanana.Connector
import com.atanana.data.{TargetTeam, Team, TeamPositionsInfo}
import com.atanana.parsers.TeamsPageParser
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.Success

class TeamPositionsInfoProviderTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  val teamPage = "teams page"
  val cityTeamsPage = "city teams page"
  val countryTeamsPage = "country teams page"

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
      setupDefaultExpectations()
      val team = createTeam(1)
      val cityTeam = createTeam(2)
      val countryTeam = createTeam(3)
      (parser.getTeams _).when(teamPage).returns(List(team))
      (parser.getTeams _).when(cityTeamsPage).returns(List(cityTeam))
      (parser.getTeams _).when(countryTeamsPage).returns(List(countryTeam))

      checkTeams(team, cityTeam, countryTeam)
    }

    "filter virtual teams" in {
      setupDefaultExpectations()
      val realTeam = createTeam(1)
      val fakeTeam = createTeam(2, isReal = false)
      (parser.getTeams _).when(teamPage).returns(List(realTeam, fakeTeam))
      (parser.getTeams _).when(cityTeamsPage).returns(List(fakeTeam, realTeam))
      (parser.getTeams _).when(countryTeamsPage).returns(List(fakeTeam, realTeam, fakeTeam))

      checkTeams(realTeam, realTeam, realTeam)
    }
  }

  private def checkTeams(team: Team, cityTeam: Team, countryTeam: Team) = {
    val targetTeam = TargetTeam("test team", "test city", 100)
    (composer.positionsInfo _).when(List(team), List(cityTeam), List(countryTeam)).returns(
      Right(TeamPositionsInfo(targetTeam, targetTeam, targetTeam, 123, 200, 3000, 20, 30))
    )

    provider.data shouldEqual Right(TeamPositionsInfo(targetTeam, targetTeam, targetTeam, 123, 200, 3000, 20, 30))
  }

  private def setupDefaultExpectations(): Unit = {
    (connector.getTeamsPage _).when().returns(Right(teamPage))
    (connector.getCityTeamsPage _).when().returns(Right(cityTeamsPage))
    (connector.getCountryTeamsPage _).when().returns(Right(countryTeamsPage))
  }

  private def createTeam(id: Int, isReal: Boolean = true): Team = Team(id, "", "", 0, 0f, isReal)
}
