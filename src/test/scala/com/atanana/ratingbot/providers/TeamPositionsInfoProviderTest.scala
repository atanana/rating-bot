package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.*
import com.atanana.ratingbot.Conversions.fromIntToReleaseId
import com.atanana.ratingbot.TestUtils.awaitEither
import com.atanana.ratingbot.data.{TargetTeam, Team, TeamPositionsInfo}
import com.atanana.ratingbot.mocks.{MockReleasesProvider, MockTeamPositionsInfoComposer, MockTeamsPageParser}
import com.atanana.ratingbot.net.{ConnectorImpl, MockConnector}
import com.atanana.ratingbot.parsers.TeamsPageParserImpl
import com.atanana.ratingbot.providers.TeamPositionsInfoProviderImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.util.chaining.scalaUtilChainingOps

class TeamPositionsInfoProviderTest extends AnyWordSpecLike with Matchers {
  private val teamPage = "teams page"
  private val cityTeamsPage = "city teams page"
  private val countryTeamsPage = "country teams page"
  private val lastReleaseId = 123

  private val connector = new MockConnector()
  private val parser = new MockTeamsPageParser()
  private val composer = new MockTeamPositionsInfoComposer()
  private val releasesProvider = new MockReleasesProvider()
  private val provider = new TeamPositionsInfoProviderImpl(connector, parser, composer, releasesProvider)

  "TeamPositionsInfoProvider" should {
    "provide correct info" in {
      setupDefaultExpectations()
      val team = createTeam(1)
      val cityTeam = createTeam(2)
      val countryTeam = createTeam(3)
      parser.teams.put(teamPage, List(team))
      parser.teams.put(cityTeamsPage, List(cityTeam))
      parser.teams.put(countryTeamsPage, List(countryTeam))

      checkTeams(team, cityTeam, countryTeam)
    }
  }

  private def checkTeams(team: Team, cityTeam: Team, countryTeam: Team) = {
    val targetTeam = TargetTeam("test team", "test city", 100)
    composer.responses.put(
      (List(team), List(cityTeam), List(countryTeam)),
      Right(TeamPositionsInfo(targetTeam.some, targetTeam.some, targetTeam, 123, 200, 3000, 20, 30))
    )

    provider.data.pipe(awaitEither) shouldEqual Right(TeamPositionsInfo(targetTeam.some, targetTeam.some, targetTeam, 123, 200, 3000, 20, 30))
  }

  private def setupDefaultExpectations(): Unit = {
    connector.teamsPageResponses.put(lastReleaseId, EitherT.rightT(teamPage))
    connector.cityTeamsPageResponses.put(lastReleaseId, EitherT.rightT(cityTeamsPage))
    connector.countryTeamsPageResponses.put(lastReleaseId, EitherT.rightT(countryTeamsPage))
    releasesProvider.releaseId = EitherT.rightT(lastReleaseId)
  }

  private def createTeam(id: Int): Team = Team(id, "", "", 0, 0)
}
