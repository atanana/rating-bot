package com.atanana

import com.atanana.net.ConnectorImpl.{API_URL, SITE_URL}
import com.atanana.TestUtils.{awaitEither, awaitError, fakeConfig}
import com.atanana.net.{ConnectorImpl, ConnectorException, MockNetWrapper, NetWrapperImpl}
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class ConnectorImplTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {

  private val config = fakeConfig
  private val wrapper = new MockNetWrapper()

  private val connector = new ConnectorImpl(wrapper, config)

  after {
    wrapper.clear()
  }

  "Connector" should {

    "get team page by wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      wrapper.pageResponses.put(teamUrl, Future.successful(Right("team page")))
      connector.getTeamPage.pipe(awaitEither) shouldEqual Right("team page")
    }

    "pass team page error from wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      wrapper.pageResponses.put(teamUrl, Future.successful(Left("123")))
      val exception = connector.getTeamPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamUrl\n123"
    }

    "wrap page error from wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      wrapper.pageResponses.put(teamUrl, Future.failed(new RuntimeException("123")))
      val exception = connector.getTeamPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }

    "get tournament page by wrapper" in {
      val tournamentId = 111
      wrapper.pageResponses.put(uri"$SITE_URL/tournament/$tournamentId", Future.successful(Right("tournament page")))
      connector.getTournamentPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament page")
    }

    "pass tournament page error from wrapper" in {
      val tournamentId = 111
      val tournamentUrl = uri"$SITE_URL/tournament/$tournamentId"
      wrapper.pageResponses.put(tournamentUrl, Future.failed(new RuntimeException("123")))
      val exception = connector.getTournamentPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }

    "get requisitions page by wrapper" in {
      wrapper.pageResponses.put(uri"$SITE_URL/synch_town/${config.city}", Future.successful(Right("requisitions page")))
      connector.getRequisitionPage.pipe(awaitEither) shouldEqual Right("requisitions page")
    }

    "pass requisitions page error from wrapper" in {
      val requisitionsUrl = uri"$SITE_URL/synch_town/${config.city}"
      wrapper.pageResponses.put(requisitionsUrl, Future.successful(Left("123")))
      val exception = connector.getRequisitionPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $requisitionsUrl\n123"
    }

    "get teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=&form[releaseId]=$releaseId", Future.successful(Right("teams page")))
      connector.getTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=&form[releaseId]=$releaseId"
      wrapper.pageResponses.put(teamsUrl, Future.successful(Left("123")))
      val exception = connector.getTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get city teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=${config.city}&form[countryId]=&form[releaseId]=$releaseId", Future.successful(Right("city teams page")))
      connector.getCityTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=${config.city}&form[countryId]=&form[releaseId]=$releaseId"
      wrapper.pageResponses.put(teamsUrl, Future.successful(Left("123")))
      val exception = connector.getCityTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get country teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=${config.country}&form[releaseId]=$releaseId", Future.successful(Right("country teams page")))
      connector.getCountryTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("country teams page")
    }

    "pass country teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=${config.country}&form[releaseId]=$releaseId"
      wrapper.pageResponses.put(teamsUrl, Future.successful(Left("123")))
      val exception = connector.getCountryTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      wrapper.pageResponses.put(uri"$SITE_URL/tournament/$tournamentId/requests", Future.successful(Right("tournament requests page")))
      connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament requests page")
    }

    "pass tournament requisitions page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$SITE_URL/tournament/$tournamentId/requests"
      wrapper.pageResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      wrapper.apiResponses.put(uri"$API_URL/tournaments/$tournamentId", Future.successful(Right("tournament info page")))
      connector.getTournamentInfo(tournamentId).pipe(awaitEither) shouldEqual Right("tournament info page")
    }

    "pass tournament info page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$API_URL/tournaments/$tournamentId"
      wrapper.apiResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getTournamentInfo(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get releases page by wrapper" in {
      wrapper.apiResponses.put(uri"$API_URL/releases?pagination=false", Future.successful(Right("releases page")))
      connector.getReleases.pipe(awaitEither) shouldEqual Right("releases page")
    }

    "pass releases page error from wrapper" in {
      val url = uri"$API_URL/releases?pagination=false"
      wrapper.apiResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getReleases.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "pass post error from wrapper" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      wrapper.postResponses.put((uri, params), Future.successful(Left("123")))
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $uri\n123 with params Map()"
    }

    "pass post error from wrapper async" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      wrapper.postResponses.put((uri, params), Future.failed(new RuntimeException("123")))
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }
  }
}
