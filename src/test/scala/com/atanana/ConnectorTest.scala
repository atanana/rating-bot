package com.atanana

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scalaj.http.HttpResponse

class ConnectorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  val config: Config = Config("token", 123, 321, 456, 10000, "test city", "test country")
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
  }
}
