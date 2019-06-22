package com.atanana.providers

import java.time.LocalDateTime

import com.atanana.Connector
import com.atanana.data.{ParsedData, PartialRequisitionData, TournamentData}
import com.atanana.parsers.{CsvParser, RequisitionsPageParser, RequisitionsParser, TournamentInfoParser}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success, Try}

class PollingDataProviderTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  var connector: Connector = _
  var csvParser: CsvParser = _
  var requisitionsParser: RequisitionsParser = _
  var requisitionsPageParser: RequisitionsPageParser = _
  var provider: PollingDataProvider = _
  var tournamentInfoParser: TournamentInfoParser = _

  before {
    connector = stub[Connector]
    csvParser = stub[CsvParser]
    requisitionsParser = stub[RequisitionsParser]
    requisitionsPageParser = stub[RequisitionsPageParser]
    tournamentInfoParser = stub[TournamentInfoParser]
    provider = new PollingDataProvider(connector, csvParser, requisitionsParser, requisitionsPageParser, tournamentInfoParser)
  }

  "PollingDataProvider" should {
    "provider valid data" in {
      val tournamentId = 123
      val tournamentRequisitionsPage = "tournament requisitions page"
      val tournamentData = mock[TournamentData]
      setTournamentsData(List(tournamentData))
      (connector.getTournamentRequisitionsPage _).when(tournamentId).returns(tournamentRequisitionsPage)
      setQuestionsCount(tournamentId, Success(36))

      val requisitionData = PartialRequisitionData("test tournament", tournamentId, "test agent", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData)))
      (requisitionsPageParser.teamsCount _).when(requisitionData.agent, tournamentRequisitionsPage).returns(Success(5))

      provider.data shouldEqual ParsedData(Set(tournamentData), Success(Set(requisitionData.toRequisitionData(36))))
    }

    "should filter small requisitions" in {
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => i.toString)
      setTournamentsData(List.empty)
      setQuestionsCount(2, Success(36))
      setQuestionsCount(3, Success(45))

      val requisitionData1 = PartialRequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.teamsCount _).when(*, *).onCall((_: String, page: String) => Success(page.toInt))

      provider.data shouldEqual ParsedData(Set.empty, Success(Set(requisitionData2.toRequisitionData(36), requisitionData3.toRequisitionData(45))))
    }

    "should filter failed requisitions" in {
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => i.toString)
      setTournamentsData(List.empty)
      setQuestionsCount(2, Success(36))

      val requisitionData1 = PartialRequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = PartialRequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = PartialRequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      setRequisitionData(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.teamsCount _).when(*, *).onCall(
        (_: String, page: String) =>
          if (page.toInt % 2 == 0) Success(2) else Failure(new RuntimeException)
      )

      provider.data shouldEqual ParsedData(Set.empty, Success(Set(requisitionData2.toRequisitionData(36))))
    }

    "should pass failed requisitions" in {
      val tournamentData = mock[TournamentData]
      setTournamentsData(List(tournamentData))
      val requisitionData = Failure(new RuntimeException)
      setRequisitionData(requisitionData)

      provider.data shouldEqual ParsedData(Set(tournamentData), requisitionData)
    }
  }

  private def setQuestionsCount(tournamentId: Int, questionsCount: Try[Int]): Unit = {
    val page = s"tournament info $tournamentId"
    (connector.getTournamentInfo _).when(tournamentId).returns(page)
    (tournamentInfoParser.getQuestionsCount _).when(page).returns(questionsCount)
  }

  private def setTournamentsData(data: List[TournamentData]): Unit = {
    val teamPage = "team page"
    (connector.getTeamPage _).when().returns(teamPage)
    (csvParser.getTournamentsData _).when(teamPage).returns(data)
  }

  private def setRequisitionData(data: Try[List[PartialRequisitionData]]): Unit = {
    val requisitionsPage = "requisitions page"
    (connector.getRequisitionPage _).when().returns(requisitionsPage)
    (requisitionsParser.getRequisitionsData _).when(requisitionsPage).returns(data)
  }
}
