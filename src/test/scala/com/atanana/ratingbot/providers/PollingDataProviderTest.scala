package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.*
import com.atanana.ratingbot.parsers.*
import com.atanana.ratingbot.Conversions.{fromIntToTeamId, fromIntToTournamentId}
import com.atanana.ratingbot.TestUtils.{awaitEither, awaitError}
import com.atanana.ratingbot.data.{ParsedData, TournamentInfo, TournamentResult}
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.mocks.{MockLastTeamResultsProvider, MockTournamentInfoProvider}
import com.atanana.ratingbot.net.MockConnector
import com.atanana.ratingbot.providers.PollingDataProviderImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.util.chaining.scalaUtilChainingOps
import scala.util.{Failure, Success, Try}

class PollingDataProviderTest extends AnyWordSpecLike with Matchers {
  private val connector = new MockConnector()
  private val tournamentInfoProvider = new MockTournamentInfoProvider()
  private val lastTeamResultsProvider = new MockLastTeamResultsProvider()
  private val config: Config = Config("tg token", "cookie", 1, 1, 1, "pipe", 1, 100, List("test venue 1", "test venue 2"))
  private val provider = new PollingDataProviderImpl(connector, tournamentInfoProvider, lastTeamResultsProvider, config)

  "PollingDataProvider" should {

    "provider valid data" in {
      val tournamentId = 123
      val tournamentResult = TournamentResult(123, 36, 3.0f, 321)
      setTournamentsData(Set(tournamentResult))
      setQuestionsCount(tournamentId, 36)

      data shouldEqual Right(ParsedData(Set(tournamentResult)))
    }
  }

  private def setQuestionsCount(tournamentId: Int, questionsCount: Int): Unit = {
    tournamentInfoProvider.tournamentInfos(tournamentId) = EitherT.rightT(TournamentInfo("test", questionsCount))
  }

  private def setTournamentsData(data: Set[TournamentResult]): Unit = {
    lastTeamResultsProvider.results(config.team) = EitherT.rightT(data)
  }

  private def data = provider.data.pipe(awaitEither)
}
