package com.atanana

import com.atanana.Connector.{API_URL, SITE_URL}
import com.atanana.TestUtils.{awaitEither, awaitError, fakeConfig}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class ConnectorTest extends AnyWordSpecLike with MockFactory with Matchers {

  private val config = fakeConfig
  private val wrapper = stub[TestWrapper]

  class TestWrapper extends NetWrapper(config)

  private val connector = new Connector(wrapper, config)

  "Connector" should {

    "get team page by wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageAsync _).when(teamUrl).returns(Future.successful(Right("team page")))
      connector.getTeamPage.pipe(awaitEither) shouldEqual Right("team page")
    }

    "pass team page error from wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageAsync _).when(teamUrl).returns(Future.successful(Left("123")))
      val exception = connector.getTeamPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamUrl\n123"
    }

    "wrap page error from wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageAsync _).when(teamUrl).returns(Future.failed(new RuntimeException("123")))
      val exception = connector.getTeamPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }

    "get tournament page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPageAsync _).when(uri"$SITE_URL/tournament/$tournamentId").returns(Future.successful(Right("tournament page")))
      connector.getTournamentPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament page")
    }

    "pass tournament page error from wrapper" in {
      val tournamentId = 111
      val tournamentUrl = uri"$SITE_URL/tournament/$tournamentId"
      (wrapper.getPageAsync _).when(tournamentUrl).returns(Future.failed(new RuntimeException("123")))
      val exception = connector.getTournamentPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }

    "get requisitions page by wrapper" in {
      (wrapper.getPageAsync _).when(uri"$SITE_URL/synch_town/${config.city}").returns(Future.successful(Right("requisitions page")))
      connector.getRequisitionPage.pipe(awaitEither) shouldEqual Right("requisitions page")
    }

    "pass requisitions page error from wrapper" in {
      val requisitionsUrl = uri"$SITE_URL/synch_town/${config.city}"
      (wrapper.getPageAsync _).when(requisitionsUrl).returns(Future.successful(Left("123")))
      val exception = connector.getRequisitionPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $requisitionsUrl\n123"
    }

    "get teams page by wrapper" in {
      val releaseId = 123
      (wrapper.getPageAsync _).when(uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[0][name]=&columns[0][searchable]=true&columns[0][orderable]=false&columns[0][search][value]=&columns[0][search][regex]=false&columns[1][data]=teamRatingPosition&columns[1][name]=&columns[1][searchable]=true&columns[1][orderable]=false&columns[1][search][value]=&columns[1][search][regex]=false&columns[2][data]=teamRating&columns[2][name]=&columns[2][searchable]=true&columns[2][orderable]=true&columns[2][search][value]=&columns[2][search][regex]=false&columns[3][data]=trb&columns[3][name]=&columns[3][searchable]=true&columns[3][orderable]=false&columns[3][search][value]=&columns[3][search][regex]=false&columns[4][data]=id&columns[4][name]=&columns[4][searchable]=true&columns[4][orderable]=true&columns[4][search][value]=&columns[4][search][regex]=false&columns[5][data]=teamName&columns[5][name]=&columns[5][searchable]=true&columns[5][orderable]=true&columns[5][search][value]=&columns[5][search][regex]=false&columns[6][data]=townName&columns[6][name]=&columns[6][searchable]=true&columns[6][orderable]=false&columns[6][search][value]=&columns[6][search][regex]=false&columns[7][data]=playedTournaments&columns[7][name]=&columns[7][searchable]=true&columns[7][orderable]=false&columns[7][search][value]=&columns[7][search][regex]=false&columns[8][data]=tournamentsPlayedInSeason&columns[8][name]=&columns[8][searchable]=true&columns[8][orderable]=true&columns[8][search][value]=&columns[8][search][regex]=false&columns[9][data]=tournamentsPlayedB&columns[9][name]=&columns[9][searchable]=true&columns[9][orderable]=true&columns[9][search][value]=&columns[9][search][regex]=false&order[0][column]=2&order[0][dir]=desc&start=0&length=500&search[value]=&search[regex]=false&form[search]=&form[townId]=&form[regionId]=&form[countryId]=&form[releaseId]=$releaseId&form[length]=500&form[start]=0").returns(Future.successful(Right("teams page")))
      connector.getTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[0][name]=&columns[0][searchable]=true&columns[0][orderable]=false&columns[0][search][value]=&columns[0][search][regex]=false&columns[1][data]=teamRatingPosition&columns[1][name]=&columns[1][searchable]=true&columns[1][orderable]=false&columns[1][search][value]=&columns[1][search][regex]=false&columns[2][data]=teamRating&columns[2][name]=&columns[2][searchable]=true&columns[2][orderable]=true&columns[2][search][value]=&columns[2][search][regex]=false&columns[3][data]=trb&columns[3][name]=&columns[3][searchable]=true&columns[3][orderable]=false&columns[3][search][value]=&columns[3][search][regex]=false&columns[4][data]=id&columns[4][name]=&columns[4][searchable]=true&columns[4][orderable]=true&columns[4][search][value]=&columns[4][search][regex]=false&columns[5][data]=teamName&columns[5][name]=&columns[5][searchable]=true&columns[5][orderable]=true&columns[5][search][value]=&columns[5][search][regex]=false&columns[6][data]=townName&columns[6][name]=&columns[6][searchable]=true&columns[6][orderable]=false&columns[6][search][value]=&columns[6][search][regex]=false&columns[7][data]=playedTournaments&columns[7][name]=&columns[7][searchable]=true&columns[7][orderable]=false&columns[7][search][value]=&columns[7][search][regex]=false&columns[8][data]=tournamentsPlayedInSeason&columns[8][name]=&columns[8][searchable]=true&columns[8][orderable]=true&columns[8][search][value]=&columns[8][search][regex]=false&columns[9][data]=tournamentsPlayedB&columns[9][name]=&columns[9][searchable]=true&columns[9][orderable]=true&columns[9][search][value]=&columns[9][search][regex]=false&order[0][column]=2&order[0][dir]=desc&start=0&length=500&search[value]=&search[regex]=false&form[search]=&form[townId]=&form[regionId]=&form[countryId]=&form[releaseId]=$releaseId&form[length]=500&form[start]=0"
      (wrapper.getPageAsync _).when(teamsUrl).returns(Future.successful(Left("123")))
      val exception = connector.getTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get city teams page by wrapper" in {
      val releaseId = 123
      (wrapper.getPageAsync _).when(uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[0][name]=&columns[0][searchable]=true&columns[0][orderable]=false&columns[0][search][value]=&columns[0][search][regex]=false&columns[1][data]=teamRatingPosition&columns[1][name]=&columns[1][searchable]=true&columns[1][orderable]=false&columns[1][search][value]=&columns[1][search][regex]=false&columns[2][data]=teamRating&columns[2][name]=&columns[2][searchable]=true&columns[2][orderable]=true&columns[2][search][value]=&columns[2][search][regex]=false&columns[3][data]=trb&columns[3][name]=&columns[3][searchable]=true&columns[3][orderable]=false&columns[3][search][value]=&columns[3][search][regex]=false&columns[4][data]=id&columns[4][name]=&columns[4][searchable]=true&columns[4][orderable]=true&columns[4][search][value]=&columns[4][search][regex]=false&columns[5][data]=teamName&columns[5][name]=&columns[5][searchable]=true&columns[5][orderable]=true&columns[5][search][value]=&columns[5][search][regex]=false&columns[6][data]=townName&columns[6][name]=&columns[6][searchable]=true&columns[6][orderable]=false&columns[6][search][value]=&columns[6][search][regex]=false&columns[7][data]=playedTournaments&columns[7][name]=&columns[7][searchable]=true&columns[7][orderable]=false&columns[7][search][value]=&columns[7][search][regex]=false&columns[8][data]=tournamentsPlayedInSeason&columns[8][name]=&columns[8][searchable]=true&columns[8][orderable]=true&columns[8][search][value]=&columns[8][search][regex]=false&columns[9][data]=tournamentsPlayedB&columns[9][name]=&columns[9][searchable]=true&columns[9][orderable]=true&columns[9][search][value]=&columns[9][search][regex]=false&order[0][column]=2&order[0][dir]=desc&start=0&length=500&search[value]=&search[regex]=false&form[search]=&form[townId]=${config.city}&form[regionId]=&form[countryId]=&form[releaseId]=$releaseId&form[length]=500&form[start]=0").returns(Future.successful(Right("city teams page")))
      connector.getCityTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[0][name]=&columns[0][searchable]=true&columns[0][orderable]=false&columns[0][search][value]=&columns[0][search][regex]=false&columns[1][data]=teamRatingPosition&columns[1][name]=&columns[1][searchable]=true&columns[1][orderable]=false&columns[1][search][value]=&columns[1][search][regex]=false&columns[2][data]=teamRating&columns[2][name]=&columns[2][searchable]=true&columns[2][orderable]=true&columns[2][search][value]=&columns[2][search][regex]=false&columns[3][data]=trb&columns[3][name]=&columns[3][searchable]=true&columns[3][orderable]=false&columns[3][search][value]=&columns[3][search][regex]=false&columns[4][data]=id&columns[4][name]=&columns[4][searchable]=true&columns[4][orderable]=true&columns[4][search][value]=&columns[4][search][regex]=false&columns[5][data]=teamName&columns[5][name]=&columns[5][searchable]=true&columns[5][orderable]=true&columns[5][search][value]=&columns[5][search][regex]=false&columns[6][data]=townName&columns[6][name]=&columns[6][searchable]=true&columns[6][orderable]=false&columns[6][search][value]=&columns[6][search][regex]=false&columns[7][data]=playedTournaments&columns[7][name]=&columns[7][searchable]=true&columns[7][orderable]=false&columns[7][search][value]=&columns[7][search][regex]=false&columns[8][data]=tournamentsPlayedInSeason&columns[8][name]=&columns[8][searchable]=true&columns[8][orderable]=true&columns[8][search][value]=&columns[8][search][regex]=false&columns[9][data]=tournamentsPlayedB&columns[9][name]=&columns[9][searchable]=true&columns[9][orderable]=true&columns[9][search][value]=&columns[9][search][regex]=false&order[0][column]=2&order[0][dir]=desc&start=0&length=500&search[value]=&search[regex]=false&form[search]=&form[townId]=${config.city}&form[regionId]=&form[countryId]=&form[releaseId]=$releaseId&form[length]=500&form[start]=0"
      (wrapper.getPageAsync _).when(teamsUrl).returns(Future.successful(Left("123")))
      val exception = connector.getCityTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get country teams page by wrapper" in {
      (wrapper.getPageAsync _).when(uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C").returns(Future.successful(Right("country teams page")))
      connector.getCountryTeamsPage.pipe(awaitEither) shouldEqual Right("country teams page")
    }

    "pass country teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C"
      (wrapper.getPageAsync _).when(teamsUrl).returns(Future.successful(Left("123")))
      val exception = connector.getCountryTeamsPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPageAsync _).when(uri"$SITE_URL/tournament/$tournamentId/requests").returns(Future.successful(Right("tournament requests page")))
      connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament requests page")
    }

    "pass tournament requisitions page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$SITE_URL/tournament/$tournamentId/requests"
      (wrapper.getPageAsync _).when(url).returns(Future.successful(Left("123")))
      val exception = connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      (wrapper.getApi _).when(uri"$API_URL/tournaments/$tournamentId").returns(Future.successful(Right("tournament info page")))
      connector.getTournamentInfo(tournamentId).pipe(awaitEither) shouldEqual Right("tournament info page")
    }

    "pass tournament info page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$API_URL/tournaments/$tournamentId"
      (wrapper.getApi _).when(url).returns(Future.successful(Left("123")))
      val exception = connector.getTournamentInfo(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get releases page by wrapper" in {
      (wrapper.getApi _).when(uri"$API_URL/releases?pagination=false").returns(Future.successful(Right("releases page")))
      connector.getReleases.pipe(awaitEither) shouldEqual Right("releases page")
    }

    "pass releases page error from wrapper" in {
      val url = uri"$API_URL/releases?pagination=false"
      (wrapper.getApi _).when(url).returns(Future.successful(Left("123")))
      val exception = connector.getReleases.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "pass post error from wrapper" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      (wrapper.postAsync _).when(uri, params).returns(Future.successful(Left("123")))
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $uri\n123 with params Map()"
    }

    "pass post error from wrapper async" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      (wrapper.postAsync _).when(uri, params).returns(Future.failed(new RuntimeException("123")))
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }
  }
}
