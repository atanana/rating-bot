package com.atanana.ratingbot

import cats.effect.IO
import com.atanana.ratingbot.Conversions.{fromIntToReleaseId, fromIntToTeamId, fromIntToTournamentId}
import com.atanana.ratingbot.TestUtils.{awaitEither, awaitError, fakeConfig}
import com.atanana.ratingbot.net.{ConnectorException, ConnectorImpl, MockNetWrapper, UriComposer}
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import sttp.client3.UriContext

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
      wrapper.pageResponses.put(UriComposer.tournamentPageUri(tournamentId), IO.pure(Right("tournament page")))
      connector.getTournamentPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament page")
    }

    "pass tournament page error from wrapper" in {
      val tournamentId = 111
      val tournamentUrl = UriComposer.tournamentPageUri(tournamentId)
      wrapper.pageResponses.put(tournamentUrl, IO {
        throw new RuntimeException("123")
      })
      val exception = connector.getTournamentPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }

    "get requisitions page by wrapper" in {
      wrapper.pageResponses.put(UriComposer.requisitionPageUri(config.city), IO.pure(Right("requisitions page")))
      connector.getRequisitionPage.pipe(awaitEither) shouldEqual Right("requisitions page")
    }

    "pass requisitions page error from wrapper" in {
      val requisitionsUrl = UriComposer.requisitionPageUri(config.city)
      wrapper.pageResponses.put(requisitionsUrl, IO.pure(Left("123")))
      val exception = connector.getRequisitionPage.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $requisitionsUrl\n123"
    }

    "get teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(UriComposer.teamsPageUri(releaseId), IO.pure(Right("teams page")))
      connector.getTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("teams page")
    }

    "pass teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = UriComposer.teamsPageUri(releaseId)
      wrapper.pageResponses.put(teamsUrl, IO.pure(Left("123")))
      val exception = connector.getTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get city teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(UriComposer.cityTeamsPageUri(releaseId, config.city), IO.pure(Right("city teams page")))
      connector.getCityTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("city teams page")
    }

    "pass city teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = UriComposer.cityTeamsPageUri(releaseId, config.city)
      wrapper.pageResponses.put(teamsUrl, IO.pure(Left("123")))
      val exception = connector.getCityTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get country teams page by wrapper" in {
      val releaseId = 123
      wrapper.pageResponses.put(UriComposer.countryTeamsPageUri(releaseId, config.country), IO.pure(Right("country teams page")))
      connector.getCountryTeamsPage(releaseId).pipe(awaitEither) shouldEqual Right("country teams page")
    }

    "pass country teams page error from wrapper" in {
      val releaseId = 123
      val teamsUrl = UriComposer.countryTeamsPageUri(releaseId, config.country)
      wrapper.pageResponses.put(teamsUrl, IO.pure(Left("123")))
      val exception = connector.getCountryTeamsPage(releaseId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $teamsUrl\n123"
    }

    "get tournament requisitions page by wrapper" in {
      val tournamentId = 111
      wrapper.pageResponses.put(UriComposer.tournamentRequisitionsPageUri(tournamentId), IO.pure(Right("tournament requests page")))
      connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament requests page")
    }

    "pass tournament requisitions page error from wrapper" in {
      val tournamentId = 111
      val url = UriComposer.tournamentRequisitionsPageUri(tournamentId)
      wrapper.pageResponses.put(url, IO.pure(Left("123")))
      val exception = connector.getTournamentRequisitionsPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get tournament info page by wrapper" in {
      val tournamentId = 111
      wrapper.apiResponses.put(UriComposer.tournamentInfoUri(tournamentId), IO.pure(Right("tournament info page")))
      connector.getTournamentInfo(tournamentId).pipe(awaitEither) shouldEqual Right("tournament info page")
    }

    "pass tournament info page error from wrapper" in {
      val tournamentId = 111
      val url = UriComposer.tournamentInfoUri(tournamentId)
      wrapper.apiResponses.put(url, IO.pure(Left("123")))
      val exception = connector.getTournamentInfo(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get releases page by wrapper" in {
      wrapper.apiResponses.put(UriComposer.releasesUri, IO.pure(Right("releases page")))
      connector.getReleases.pipe(awaitEither) shouldEqual Right("releases page")
    }

    "pass releases page error from wrapper" in {
      val url = UriComposer.releasesUri
      wrapper.apiResponses.put(url, IO.pure(Left("123")))
      val exception = connector.getReleases.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get team tournaments page by wrapper" in {
      wrapper.apiResponses.put(UriComposer.teamTournamentsUri(config.team), IO.pure(Right("team tournaments page")))
      connector.getTeamTournaments.pipe(awaitEither) shouldEqual Right("team tournaments page")
    }

    "pass team tournaments page error from wrapper" in {
      val url = UriComposer.teamTournamentsUri(config.team)
      wrapper.apiResponses.put(url, IO.pure(Left("123")))
      val exception = connector.getTeamTournaments.pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "get tournament results page by wrapper" in {
      val tournamentId = 123
      wrapper.apiResponses.put(UriComposer.tournamentResultsUri(tournamentId), IO.pure(Right("tournament results page")))
      connector.getTournamentResultsPage(tournamentId).pipe(awaitEither) shouldEqual Right("tournament results page")
    }

    "pass tournament results page error from wrapper" in {
      val tournamentId = 123
      val url = UriComposer.tournamentResultsUri(tournamentId)
      wrapper.apiResponses.put(url, IO.pure(Left("123")))
      val exception = connector.getTournamentResultsPage(tournamentId).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $url\n123"
    }

    "pass post error from wrapper" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      wrapper.postResponses.put((uri, params), IO.pure(Left("123")))
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception should have message s"Error uri: $uri\n123 with params Map()"
    }

    "pass post error from wrapper async" in {
      val uri = uri"http://test.com"
      val params = Map.empty[String, String]
      wrapper.postResponses.put((uri, params), IO {
        throw new RuntimeException("123")
      })
      val exception = connector.postAsync(uri, params).pipe(awaitError)
      exception shouldBe a[ConnectorException]
      exception.getCause should have message "123"
    }
  }
}
