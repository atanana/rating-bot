package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.implicits.*
import com.atanana.parsers.*
import com.atanana.ratingbot.Conversions.{fromIntToTeamId, fromIntToTournamentId}
import com.atanana.ratingbot.TestUtils.{awaitEither, awaitError}
import com.atanana.ratingbot.data.{ParsedData, PartialRequisitionData, TournamentInfo, TournamentResult}
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.mocks.{MockLastTeamResultsProvider, MockRequisitionsPageParser, MockRequisitionsParser, MockTournamentInfoProvider}
import com.atanana.ratingbot.net.MockConnector
import com.atanana.ratingbot.parsers.RequisitionAdditionalData
import com.atanana.ratingbot.providers.PollingDataProviderImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps
import scala.util.{Failure, Success, Try}

class PollingDataProviderTest extends AnyWordSpecLike with Matchers {
  private val connector = new MockConnector()
  private val requisitionsParser = new MockRequisitionsParser()
  private val requisitionsPageParser = new MockRequisitionsPageParser()
  private val tournamentInfoProvider = new MockTournamentInfoProvider()
  private val lastTeamResultsProvider = new MockLastTeamResultsProvider()
  private val config: Config = Config("tg token", "cookie", 1, 1, 1, 1, 1, List("test venue 1", "test venue 2"))
  private val provider = new PollingDataProviderImpl(connector, requisitionsParser, requisitionsPageParser, tournamentInfoProvider, lastTeamResultsProvider, config)

  "PollingDataProvider" should {

    "provider valid data" in {
      val tournamentId = 123
      val tournamentRequisitionsPage = "tournament requisitions page"
      val tournamentResult = TournamentResult(123, 36, 3.0f, 321)
      setTournamentsData(Set(tournamentResult))
      connector.tournamentRequisitionsPageResponses.put(tournamentId, EitherT.rightT[Future, Throwable](tournamentRequisitionsPage))
      setQuestionsCount(tournamentId, 36)

      val requisitionData = PartialRequisitionData("test tournament", tournamentId, "test agent", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData)))
      requisitionsPageParser.data.put((requisitionData.agent, tournamentRequisitionsPage), Success(RequisitionAdditionalData("test", 5)))

      data shouldEqual Right(ParsedData(Set(tournamentResult), Set(requisitionData.toRequisitionData(36))))
    }

    "should filter small requisitions" in {
      connector.tournamentRequisitionsPageResponses.put(1, EitherT.rightT[Future, Throwable]("1"))
      connector.tournamentRequisitionsPageResponses.put(2, EitherT.rightT[Future, Throwable]("2"))
      connector.tournamentRequisitionsPageResponses.put(3, EitherT.rightT[Future, Throwable]("3"))
      setTournamentsData(Set.empty)
      setQuestionsCount(2, 36)
      setQuestionsCount(3, 45)

      val requisitionData1 = PartialRequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      requisitionsPageParser.data.put(("test agent 1", "1"), Success(RequisitionAdditionalData("test", 1)))
      requisitionsPageParser.data.put(("test agent 2", "2"), Success(RequisitionAdditionalData("test", 2)))
      requisitionsPageParser.data.put(("test agent 3", "3"), Success(RequisitionAdditionalData("test", 3)))

      data shouldEqual Right(ParsedData(Set.empty, Set(requisitionData2.toRequisitionData(36), requisitionData3.toRequisitionData(45))))
    }

    "should filter requisitions from ignored venues" in {
      connector.tournamentRequisitionsPageResponses.put(1, EitherT.rightT[Future, Throwable]("1"))
      connector.tournamentRequisitionsPageResponses.put(2, EitherT.rightT[Future, Throwable]("2"))
      connector.tournamentRequisitionsPageResponses.put(3, EitherT.rightT[Future, Throwable]("3"))
      setTournamentsData(Set.empty)
      setQuestionsCount(1, 36)
      setQuestionsCount(2, 36)
      setQuestionsCount(3, 45)

      val requisitionData1 = PartialRequisitionData("test tournament", 3, "test venue 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test venue 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test venue 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      requisitionsPageParser.data.put(("test venue 1", "3"), Success(RequisitionAdditionalData("test venue 1", 1)))
      requisitionsPageParser.data.put(("test venue 2", "2"), Success(RequisitionAdditionalData("test venue 2", 2)))
      requisitionsPageParser.data.put(("test venue 3", "3"), Success(RequisitionAdditionalData("test venue 3", 3)))

      data shouldEqual Right(ParsedData(Set.empty, Set(requisitionData3.toRequisitionData(45))))
    }

    "should pass failed requisitions" in {
      val tournamentResult = TournamentResult(123, 36, 3.0f, 321)
      setTournamentsData(Set(tournamentResult))
      val requisitionData = Failure(new RuntimeException("123"))
      setRequisitionData(requisitionData)

      provider.data.pipe(awaitError) should have message "123"
    }
  }

  private def setQuestionsCount(tournamentId: Int, questionsCount: Int): Unit = {
    tournamentInfoProvider.tournamentInfos(tournamentId) = EitherT.rightT(TournamentInfo("test", questionsCount))
  }

  private def setTournamentsData(data: Set[TournamentResult]): Unit = {
    lastTeamResultsProvider.results(config.team) = EitherT.rightT(data)
  }

  private def setRequisitionData(data: Try[List[PartialRequisitionData]]): Unit = {
    val requisitionsPage = "requisitions page"
    connector.requisitionPage = EitherT.rightT(requisitionsPage)
    requisitionsParser.requisitions.put(requisitionsPage, data)
  }

  private def data = provider.data.pipe(awaitEither)
}
