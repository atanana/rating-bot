package com.atanana

import com.atanana.json.Config
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}
import scalaj.http.HttpResponse

class ConnectorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  val config: Config = Config("token", 123, 321, 456, 10000, "Минск", "Беларусь", List.empty)
  var wrapper: NetWrapper = _
  var connector: Connector = _

  before {
    wrapper = stub[NetWrapper]
    connector = new Connector(wrapper, config)
  }

  "Connector" should {
    "get by wrapper" in {
      val url = "test url"
      (wrapper.get _).when(url).returns(HttpResponse("test get", 100, Map.empty))
      connector.get(url) shouldEqual HttpResponse("test get", 100, Map.empty)
    }

    "get team page by wrapper" in {
      (wrapper.getPage _).when(Connector.SITE_URL + s"/teams.php?team_id=${config.team}&download_data=export_tournaments").returns("team page")
      connector.getTeamPage shouldEqual "team page"
    }

    "get tournament page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(Connector.SITE_URL + "/tournament/" + tournamentId).returns("tournament page")
      connector.getTournamentPage(tournamentId) shouldEqual "tournament page"
    }

    "get requisitions page by wrapper" in {
      (wrapper.getPage _).when(Connector.SITE_URL + "/synch_town/" + config.city).returns("requisitions page")
      connector.getRequisitionPage shouldEqual "requisitions page"
    }

    "get teams page by wrapper" in {
      (wrapper.getPage _).when(Connector.SITE_URL + "/teams.php").returns("teams page")
      connector.getTeamsPage shouldEqual "teams page"
    }

    "get city teams page by wrapper" in {
      (wrapper.getPage _).when(Connector.SITE_URL + "/teams.php?town=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA").returns("city teams page")
      connector.getCityTeamsPage shouldEqual "city teams page"
    }

    "get country teams page by wrapper" in {
      (wrapper.getPage _).when(Connector.SITE_URL + "/teams.php?country=%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C").returns("country teams page")
      connector.getCountryTeamsPage shouldEqual "country teams page"
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(Connector.SITE_URL + s"/tournament/$tournamentId/requests").returns("tournament requests page")
      connector.getTournamentRequisitionsPage(tournamentId) shouldEqual "tournament requests page"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      (wrapper.getPage _).when(Connector.SITE_URL + s"/api/tournaments/$tournamentId.json").returns("tournament info page")
      connector.getTournamentInfo(tournamentId) shouldEqual "tournament info page"
    }
  }
}
