package com.atanana

import com.atanana.Connector.SITE_URL
import com.atanana.json.Config
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

class ConnectorTest extends AnyWordSpecLike with MockFactory with Matchers {

  val config: Config = Config("token", 123, 321, 456, 10000, "Минск", "Беларусь", List.empty)
  var wrapper: NetWrapper = stub[NetWrapper]
  var connector: Connector = new Connector(wrapper, config)

  "Connector" should {

    "get team page by wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageSafe _).when(teamUrl).returns(Right("team page"))
      connector.getTeamPage shouldEqual Right("team page")
    }

    "pass team page error from wrapper" in {
      val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
      (wrapper.getPageSafe _).when(teamUrl).returns(Left("123"))
      connector.getTeamPage shouldEqual Left(s"Cannot get team's page($teamUrl): 123")
    }

    "get tournament page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPageSafe _).when(uri"$SITE_URL/tournament/$tournamentId").returns(Right("tournament page"))
      connector.getTournamentPage(tournamentId) shouldEqual Right("tournament page")
    }

    "pass tournament page error from wrapper" in {
      val tournamentId = 111
      val tournamentUrl = uri"$SITE_URL/tournament/$tournamentId"
      (wrapper.getPageSafe _).when(tournamentUrl).returns(Left("123"))
      connector.getTournamentPage(tournamentId) shouldEqual Left(s"Cannot get tournament's page($tournamentUrl): 123")
    }

    "get requisitions page by wrapper" in {
      (wrapper.getPageSafe _).when(uri"$SITE_URL/synch_town/${config.city}").returns(Right("requisitions page"))
      connector.getRequisitionPage shouldEqual Right("requisitions page")
    }

    "pass requisitions page error from wrapper" in {
      val requisitionsUrl = uri"$SITE_URL/synch_town/${config.city}"
      (wrapper.getPageSafe _).when(requisitionsUrl).returns(Left("123"))
      connector.getRequisitionPage shouldEqual Left(s"Cannot get requisitions page($requisitionsUrl): 123")
    }

    "get teams page by wrapper" in {
      (wrapper.getPageSafe _).when(uri"$SITE_URL/teams.php").returns(Right("teams page"))
      connector.getTeamsPage shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php"
      (wrapper.getPageSafe _).when(teamsUrl).returns(Left("123"))
      connector.getTeamsPage shouldEqual Left(s"Cannot get teams page($teamsUrl): 123")
    }

    "get city teams page by wrapper" in {
      (wrapper.getPageSafe _).when(uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA").returns(Right("city teams page"))
      connector.getCityTeamsPage shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA"
      (wrapper.getPageSafe _).when(teamsUrl).returns(Left("123"))
      connector.getCityTeamsPage shouldEqual Left(s"Cannot get city teams page($teamsUrl): 123")
    }

    "get country teams page by wrapper" in {
      (wrapper.getPageSafe _).when(uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C").returns(Right("country teams page"))
      connector.getCountryTeamsPage shouldEqual Right("country teams page")
    }

    "pass country teams page error from wrapper" in {
      val teamsUrl = uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C"
      (wrapper.getPageSafe _).when(teamsUrl).returns(Left("123"))
      connector.getCountryTeamsPage shouldEqual Left(s"Cannot get country teams page($teamsUrl): 123")
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPageSafe _).when(uri"$SITE_URL/tournament/$tournamentId/requests").returns(Right("tournament requests page"))
      connector.getTournamentRequisitionsPage(tournamentId) shouldEqual Right("tournament requests page")
    }

    "pass tournament requisitions page error from wrapper" in {
      val tournamentId = 111
      val url = uri"$SITE_URL/tournament/$tournamentId/requests"
      (wrapper.getPageSafe _).when(url).returns(Left("123"))
      connector.getTournamentRequisitionsPage(tournamentId) shouldEqual Left(s"Cannot ge tournament requisitions page($url): 123")
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(uri"$SITE_URL/api/tournaments/$tournamentId.json").returns("tournament info page")
      connector.getTournamentInfo(tournamentId) shouldEqual "tournament info page"
    }
  }
}
