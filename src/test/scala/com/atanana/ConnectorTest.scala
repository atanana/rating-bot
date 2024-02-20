package com.atanana

import com.atanana.TestUtils.{awaitEither, awaitError, fakeConfig}
import com.atanana.net.ConnectorImpl.{API_URL, SITE_URL}
import com.atanana.net.{ConnectorException, ConnectorImpl, MockNetWrapper, NetWrapperImpl, UriComposer}
import com.atanana.types.Ids.TournamentId
import com.atanana.Conversions.fromIntToTournamentId
import com.atanana.Conversions.fromIntToReleaseId
import com.atanana.Conversions.fromIntToTeamId
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class ConnectorTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {

  private val config = fakeConfig
  private val wrapper = new MockNetWrapper()

  private val connector = new ConnectorImpl(wrapper, config)

  after {
    wrapper.clear()
  }

  "Connector" should {

    "get tournament page by wrapper" in {
      val tournamentId = 111
      wrapper.pageResponses.put(UriComposer.tournamentPageUri(tournamentId), Future.successful(Right("tournament page")))
      connector.getTournamentPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament page")
    }

    "pass tournament page error from wrapper" in {
      val tournamentId = 111
      val tournamentUrl = UriComposer.tournamentPageUri(tournamentId)
      wrapper.pageResponses.put(tournamentUrl, Future.failed(new RuntimeException("123")))
      val exception = connector.getTournamentPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }

    "get requisitions page by wrapper" in {
      wrapper.pageResponses.put(UriComposer.requisitionPageUri(config.city), Future.successful(Right("requisitions page")))
      connector.getRequisitionPage.pipe(awaitEither) shouldEqual Right("requisitions page")
    }

    "pass requisitions page error from wrapper" in {
      val requisitionsUrl = UriComposer.requisitionPageUri(config.city)
      wrapper.pageResponses.put(requisitionsUrl, Future.successful(Left("123")))
      val exception = connector.getRequisitionPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $requisitionsUrl\n123"
    }

    "get teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(UriComposer.teamsPageUri(releaseId), Future.successful(Right("teams page")))
      connector.getTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = UriComposer.teamsPageUri(releaseId)
      wrapper.pageResponses.put(teamsUrl, Future.successful(Left("123")))
      val exception = connector.getTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get city teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(UriComposer.cityTeamsPageUri(releaseId, config.city), Future.successful(Right("city teams page")))
      connector.getCityTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = UriComposer.cityTeamsPageUri(releaseId, config.city)
      wrapper.pageResponses.put(teamsUrl, Future.successful(Left("123")))
      val exception = connector.getCityTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get country teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(UriComposer.countryTeamsPageUri(releaseId, config.country), Future.successful(Right("country teams page")))
      connector.getCountryTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("country teams page")
    }

    "pass country teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = UriComposer.countryTeamsPageUri(releaseId, config.country)
      wrapper.pageResponses.put(teamsUrl, Future.successful(Left("123")))
      val exception = connector.getCountryTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      wrapper.pageResponses.put(UriComposer.tournamentRequisitionsPageUri(tournamentId), Future.successful(Right("tournament requests page")))
      connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament requests page")
    }

    "pass tournament requisitions page error from wrapper" in {
      val tournamentId = 111
      val url = UriComposer.tournamentRequisitionsPageUri(tournamentId)
      wrapper.pageResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      wrapper.apiResponses.put(UriComposer.tournamentInfoUri(tournamentId), Future.successful(Right("tournament info page")))
      connector.getTournamentInfo(tournamentId).pipe(awaitEither) shouldEqual Right("tournament info page")
    }

    "pass tournament info page error from wrapper" in {
      val tournamentId = 111
      val url = UriComposer.tournamentInfoUri(tournamentId)
      wrapper.apiResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getTournamentInfo(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get releases page by wrapper" in {
      wrapper.apiResponses.put(UriComposer.releasesUri, Future.successful(Right("releases page")))
      connector.getReleases.pipe(awaitEither) shouldEqual Right("releases page")
    }

    "pass releases page error from wrapper" in {
      val url = UriComposer.releasesUri
      wrapper.apiResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getReleases.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get team tournaments page by wrapper" in {
      wrapper.apiResponses.put(UriComposer.teamTournamentsUri(config.team), Future.successful(Right("team tournaments page")))
      connector.getTeamTournaments.pipe(awaitEither) shouldEqual Right("team tournaments page")
    }

    "pass team tournaments page error from wrapper" in {
      val url = UriComposer.teamTournamentsUri(config.team)
      wrapper.apiResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getTeamTournaments.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get tournament results page by wrapper" in {
      val tournamentId = 123
      wrapper.apiResponses.put(UriComposer.tournamentResultsUri(tournamentId), Future.successful(Right("tournament results page")))
      connector.getTournamentResultsPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament results page")
    }

    "pass tournament results page error from wrapper" in {
      val tournamentId = 123
      val url = UriComposer.tournamentResultsUri(tournamentId)
      wrapper.apiResponses.put(url, Future.successful(Left("123")))
      val exception = connector.getTournamentResultsPage(tournamentId).pipe(awaitError)
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
