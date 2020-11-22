package com.atanana

import com.atanana.Connector.SITE_URL
import com.atanana.json.Config
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import sttp.client3.UriContext

class ConnectorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  val config: Config = Config("token", 123, 321, 456, 10000, "Минск", "Беларусь", List.empty)
  var wrapper: NetWrapper = _
  var connector: Connector = _

  before {
    wrapper = stub[NetWrapper]
    connector = new Connector(wrapper, config)
  }

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
      (wrapper.getPage _).when(uri"$SITE_URL/teams.php").returns("teams page")
      connector.getTeamsPage shouldEqual "teams page"
    }

    "get city teams page by wrapper" in {
      (wrapper.getPage _).when(uri"$SITE_URL/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA").returns("city teams page")
      connector.getCityTeamsPage shouldEqual "city teams page"
    }

    "get country teams page by wrapper" in {
      (wrapper.getPage _).when(uri"$SITE_URL/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C").returns("country teams page")
      connector.getCountryTeamsPage shouldEqual "country teams page"
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(uri"$SITE_URL/tournament/$tournamentId/requests").returns("tournament requests page")
      connector.getTournamentRequisitionsPage(tournamentId) shouldEqual "tournament requests page"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(uri"$SITE_URL/api/tournaments/$tournamentId.json").returns("tournament info page")
      connector.getTournamentInfo(tournamentId) shouldEqual "tournament info page"
    }
  }
}
