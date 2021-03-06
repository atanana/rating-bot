package com.atanana

import com.atanana.Connector.SITE_URL
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.json.Config
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class ConnectorTest extends AnyWordSpecLike with MockFactory with Matchers {

  private val config = Config("token", 123, 321, 456, 10000, "Минск", "Беларусь", List.empty)
  private val wrapper = stub[NetWrapper]
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
      exception should have message "123"
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
      exception should have message "123"
    }

    "get teams page by wrapper" in {
      (wrapper.getPageAsync _).when(uri"$SITE_URL/teams.php").returns(Future.successful(Right("teams page")))
      connector.getTeamsPage.pipe(awaitEither) shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php"
      (wrapper.getPageAsync _).when(teamsUrl).returns(Future.successful(Left("123")))
      val exception = connector.getTeamsPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message "123"
    }

    "get city teams page by wrapper" in {
      (wrapper.getPageAsync _).when(uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA").returns(Future.successful(Right("city teams page")))
      connector.getCityTeamsPage.pipe(awaitEither) shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA"
      (wrapper.getPageAsync _).when(teamsUrl).returns(Future.successful(Left("123")))
      val exception = connector.getCityTeamsPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message "123"
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
      exception should have message "123"
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
      exception should have message "123"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPageAsync _).when(uri"$SITE_URL/api/tournaments/$tournamentId.json").returns(Future.successful(Right("tournament info page")))
      connector.getTournamentInfo(tournamentId).pipe(awaitEither) shouldEqual Right("tournament info page")
    }

    "pass tournament info page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$SITE_URL/api/tournaments/$tournamentId.json"
      (wrapper.getPageAsync _).when(url).returns(Future.successful(Left("123")))
      val exception = connector.getTournamentInfo(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message "123"
    }

    "pass post error from wrapper" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      (wrapper.postAsync _).when(uri, params).returns(Future.successful(Left("123")))
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message "123 with params Map()"
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
