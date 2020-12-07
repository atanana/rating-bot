package com.atanana.providers

import com.atanana.Connector
import com.atanana.TestUtils.await
import com.atanana.data.{ParsedData, PartialRequisitionData, TournamentData}
import com.atanana.json.Config
import com.atanana.parsers._
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps
import scala.util.{Failure, Success, Try}

class PollingDataProviderTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val connector = stub[Connector]
  private val csvParser = stub[CsvParser]
  private val requisitionsParser = stub[RequisitionsParser]
  private val requisitionsPageParser = stub[RequisitionsPageParser]
  private val tournamentInfoParser = stub[TournamentInfoParser]
  private val config: Config = Config("token", 1, 1, 1, 1, "test", "test", List("test venue 1", "test venue 2"))
  private val provider = new PollingDataProvider(connector, csvParser, requisitionsParser, requisitionsPageParser, tournamentInfoParser, config)

  "PollingDataProvider" should {

    "provider valid data" in {
      val tournamentId = 123
      val tournamentRequisitionsPage = "tournament requisitions page"
      val tournamentData = mock[TournamentData]
      setTournamentsData(List(tournamentData))
      (connector.getTournamentRequisitionsPage _).when(tournamentId).returns(Future.successful(Right(tournamentRequisitionsPage)))
      setQuestionsCount(tournamentId, Success(36))

      val requisitionData = PartialRequisitionData("test tournament", tournamentId, "test agent", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData)))
      (requisitionsPageParser.additionalData _).when(requisitionData.agent, tournamentRequisitionsPage).returns(Success(RequisitionAdditionalData("test", 5)))

      data shouldEqual Right(ParsedData(Set(tournamentData), Success(Set(requisitionData.toRequisitionData(36)))))
    }

    "pass team page error" in {
      (connector.getTeamPage _).when().returns(Future.successful(Left("team page error")))
      data shouldEqual Left("team page error")
    }

    "pass team page error async" in {
      (connector.getTeamPage _).when().returns(Future.failed(new RuntimeException("123")))
      the[RuntimeException] thrownBy data should have message "123"
    }

    "should filter small requisitions" in {
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => Future.successful(Right(i.toString)))
      setTournamentsData(List.empty)
      setQuestionsCount(2, Success(36))
      setQuestionsCount(3, Success(45))

      val requisitionData1 = PartialRequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.additionalData _).when(*, *).onCall((_: String, page: String) => Success(RequisitionAdditionalData("test", page.toInt)))

      data shouldEqual Right(ParsedData(Set.empty, Success(Set(requisitionData2.toRequisitionData(36), requisitionData3.toRequisitionData(45)))))
    }

    "should filter requisitions from ignored venues" in {
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => Future.successful(Right(i.toString)))
      setTournamentsData(List.empty)
      setQuestionsCount(1, Success(36))
      setQuestionsCount(2, Success(36))
      setQuestionsCount(3, Success(45))

      val requisitionData1 = PartialRequisitionData("test tournament", 3, "test venue 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test venue 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test venue 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.additionalData _).when(*, *).onCall((agent: String, page: String) => Success(RequisitionAdditionalData(agent, page.toInt)))

      data shouldEqual Right(ParsedData(Set.empty, Success(Set(requisitionData3.toRequisitionData(45)))))
    }

    "should filter failed requisitions" in {
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => Future.successful(Right(i.toString)))
      setTournamentsData(List.empty)
      setQuestionsCount(2, Success(36))

      val requisitionData1 = PartialRequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.additionalData _).when(*, *).onCall(
        (_: String, page: String) =>
          if (page.toInt % 2 == 0) Success(RequisitionAdditionalData("test", 2)) else Failure(new RuntimeException)
      )

      data shouldEqual Right(ParsedData(Set.empty, Success(Set(requisitionData2.toRequisitionData(36)))))
    }

    "should pass failed requisitions" in {
      val tournamentData = mock[TournamentData]
      setTournamentsData(List(tournamentData))
      val requisitionData = Failure(new RuntimeException)
      setRequisitionData(requisitionData)

      data shouldEqual Right(ParsedData(Set(tournamentData), requisitionData))
    }
  }

  private def setQuestionsCount(tournamentId: Int, questionsCount: Try[Int]): Unit = {
    val page = s"tournament info $tournamentId"
    (connector.getTournamentInfo _).when(tournamentId).returns(Future.successful(Right(page)))
    (tournamentInfoParser.getQuestionsCount _).when(page).returns(questionsCount)
  }

  private def setTournamentsData(data: List[TournamentData]): Unit = {
    val teamPage = "team page"
    (connector.getTeamPage _).when().returns(Future.successful(Right(teamPage)))
    (csvParser.getTournamentsData _).when(teamPage).returns(data)
  }

  private def setRequisitionData(data: Try[List[PartialRequisitionData]]): Unit = {
    val requisitionsPage = "requisitions page"
    (connector.getRequisitionPage _).when().returns(Future.successful(Right(requisitionsPage)))
    (requisitionsParser.getRequisitionsData _).when(requisitionsPage).returns(data)
  }

  private def data = provider.data.pipe(await)
}
