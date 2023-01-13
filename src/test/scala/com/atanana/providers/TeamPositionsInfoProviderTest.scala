package com.atanana.providers

import cats.data.EitherT
import com.atanana.Connector
import com.atanana.TestUtils.awaitEither
import com.atanana.data.{TargetTeam, Team, TeamPositionsInfo}
import com.atanana.parsers.TeamsPageParser
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class TeamPositionsInfoProviderTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val teamPage = "teams page"
  private val cityTeamsPage = "city teams page"
  private val countryTeamsPage = "country teams page"
  private val lastReleaseId = 123

  private val connector = stub[Connector]
  private val parser = stub[TeamsPageParser]
  private val composer = stub[TeamPositionsInfoComposer]
  private val releasesProvider = stub[ReleasesProvider]
  private val provider = new TeamPositionsInfoProvider(connector, parser, composer, releasesProvider)

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
      Right(TeamPositionsInfo(Some(targetTeam), Some(targetTeam), targetTeam, 123, 200, 3000, 20, 30))
    )

    provider.data.pipe(awaitEither) shouldEqual Right(TeamPositionsInfo(Some(targetTeam), Some(targetTeam), targetTeam, 123, 200, 3000, 20, 30))
  }

  private def setupDefaultExpectations(): Unit = {
    (connector.getTeamsPage _).when(lastReleaseId).returns(EitherT.rightT[Future, Throwable](teamPage))
    (connector.getCityTeamsPage _).when().returns(EitherT.rightT[Future, Throwable](cityTeamsPage))
    (connector.getCountryTeamsPage _).when().returns(EitherT.rightT[Future, Throwable](countryTeamsPage))
    (releasesProvider.getLastReleaseId _).when().returns(EitherT.rightT[Future, Throwable](lastReleaseId))
  }

  private def createTeam(id: Int, isReal: Boolean = true): Team = Team(id, "", "", 0, 0f, isReal)
}
