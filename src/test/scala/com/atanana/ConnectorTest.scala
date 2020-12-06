package com.atanana

import com.atanana.Connector.SITE_URL
import com.atanana.TestUtils.await
import com.atanana.json.Config
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class ConnectorTest extends AnyWordSpecLike with MockFactory with Matchers {

  val config: Config = Config("token", 123, 321, 456, 10000, "Минск", "Беларусь", List.empty)
  var wrapper: NetWrapper = stub[NetWrapper]
  var connector: Connector = new Connector(wrapper, config)

  "Connector" should {

    "get team page by wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageAsync _).when(teamUrl).returns(Future.successful(Right("team page")))
      connector.getTeamPage.pipe(await) shouldEqual Right("team page")
    }

    "pass team page error from wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageAsync _).when(teamUrl).returns(Future.successful(Left("123")))
      connector.getTeamPage.pipe(await) shouldEqual Left(s"Cannot get team's page($teamUrl): 123")
    }

    "pass team page error from wrapper async" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageAsync _).when(teamUrl).returns(Future.failed(new RuntimeException("123")))
      the[RuntimeException] thrownBy {
        connector.getTeamPage.pipe(await)
      } should have message "123"
    }

    "get tournament page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(uri"$SITE_URL/tournament/$tournamentId").returns(Right("tournament page"))
      connector.getTournamentPage(tournamentId) shouldEqual Right("tournament page")
    }

    "pass tournament page error from wrapper" in {
      val tournamentId = 111
      val tournamentUrl = uri"$SITE_URL/tournament/$tournamentId"
      (wrapper.getPage _).when(tournamentUrl).returns(Left("123"))
      connector.getTournamentPage(tournamentId) shouldEqual Left(s"Cannot get tournament's page($tournamentUrl): 123")
    }

    "get requisitions page by wrapper" in {
      (wrapper.getPage _).when(uri"$SITE_URL/synch_town/${config.city}").returns(Right("requisitions page"))
      connector.getRequisitionPage shouldEqual Right("requisitions page")
    }

    "pass requisitions page error from wrapper" in {
      val requisitionsUrl = uri"$SITE_URL/synch_town/${config.city}"
      (wrapper.getPage _).when(requisitionsUrl).returns(Left("123"))
      connector.getRequisitionPage shouldEqual Left(s"Cannot get requisitions page($requisitionsUrl): 123")
    }

    "get teams page by wrapper" in {
      (wrapper.getPage _).when(uri"$SITE_URL/teams.php").returns(Right("teams page"))
      connector.getTeamsPage shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php"
      (wrapper.getPage _).when(teamsUrl).returns(Left("123"))
      connector.getTeamsPage shouldEqual Left(s"Cannot get teams page($teamsUrl): 123")
    }

    "get city teams page by wrapper" in {
      (wrapper.getPage _).when(uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA").returns(Right("city teams page"))
      connector.getCityTeamsPage shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA"
      (wrapper.getPage _).when(teamsUrl).returns(Left("123"))
      connector.getCityTeamsPage shouldEqual Left(s"Cannot get city teams page($teamsUrl): 123")
    }

    "get country teams page by wrapper" in {
      (wrapper.getPage _).when(uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C").returns(Right("country teams page"))
      connector.getCountryTeamsPage shouldEqual Right("country teams page")
    }

    "pass country teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C"
      (wrapper.getPage _).when(teamsUrl).returns(Left("123"))
      connector.getCountryTeamsPage shouldEqual Left(s"Cannot get country teams page($teamsUrl): 123")
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(uri"$SITE_URL/tournament/$tournamentId/requests").returns(Right("tournament requests page"))
      connector.getTournamentRequisitionsPage(tournamentId) shouldEqual Right("tournament requests page")
    }

    "pass tournament requisitions page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$SITE_URL/tournament/$tournamentId/requests"
      (wrapper.getPage _).when(url).returns(Left("123"))
      connector.getTournamentRequisitionsPage(tournamentId) shouldEqual Left(s"Cannot get tournament requisitions page($url): 123")
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(uri"$SITE_URL/api/tournaments/$tournamentId.json").returns(Right("tournament info page"))
      connector.getTournamentInfo(tournamentId) shouldEqual Right("tournament info page")
    }

    "pass tournament info page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$SITE_URL/api/tournaments/$tournamentId.json"
      (wrapper.getPage _).when(url).returns(Left("123"))
      connector.getTournamentInfo(tournamentId) shouldEqual Left(s"Cannot get tournament info page($url): 123")
    }

    "pass post error from wrapper" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      (wrapper.post _).when(uri, params).returns(Left("123"))
      connector.post(uri, params) shouldEqual Left(s"Cannot post Map() to $uri: 123")
    }

    "pass post error from wrapper async" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      (wrapper.postAsync _).when(uri, params).returns(Future.successful(Left("123")))
      connector.postAsync(uri, params).pipe(await) shouldEqual Left(s"Cannot post Map() to $uri: 123")
    }
  }
}
