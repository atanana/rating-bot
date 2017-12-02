package com.atanana.providers

import java.time.LocalDateTime

import com.atanana.Connector
import com.atanana.data.{ParsedData, RequisitionData, TournamentData}
import com.atanana.parsers.{CsvParser, RequisitionsPageParser, RequisitionsParser}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class PollingDataProviderTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  var connector: Connector = _
  var csvParser: CsvParser = _
  var requisitionsParser: RequisitionsParser = _
  var requisitionsPageParser: RequisitionsPageParser = _
  var provider: PollingDataProvider = _

  before {
    connector = stub[Connector]
    csvParser = stub[CsvParser]
    requisitionsParser = stub[RequisitionsParser]
    requisitionsPageParser = stub[RequisitionsPageParser]
    provider = new PollingDataProvider(connector, csvParser, requisitionsParser, requisitionsPageParser)
  }

  "PollingDataProvider" should {
    "provider valid data" in {
      val tournamentId = 123
      val teamPage = "team page"
      val requisitionsPage = "requisitions page"
      val tournamentRequisitionsPage = "tournament requisitions page"
      (connector.getTeamPage _).when().returns(teamPage)
      (connector.getRequisitionPage _).when().returns(requisitionsPage)
      (connector.getTournamentRequisitionsPage _).when(tournamentId).returns(tournamentRequisitionsPage)

      val tournamentData = mock[TournamentData]
      val requisitionData = RequisitionData("test tournament", tournamentId, "test agent", LocalDateTime.now())
      (csvParser.getTournamentsData _).when(teamPage).returns(List(tournamentData))
      (requisitionsParser.getRequisitionsData _).when(requisitionsPage).returns(Success(List(requisitionData)))
      (requisitionsPageParser.teamsCount _).when(requisitionData.agent, tournamentRequisitionsPage).returns(Success(5))

      provider.data shouldEqual ParsedData(Set(tournamentData), Success(Set(requisitionData)))
    }

    "should filter small requisitions" in {
      val teamPage = "team page"
      val requisitionsPage = "requisitions page"
      (connector.getTeamPage _).when().returns(teamPage)
      (connector.getRequisitionPage _).when().returns(requisitionsPage)
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => i.toString)

      val requisitionData1 = RequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = RequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = RequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      (csvParser.getTournamentsData _).when(teamPage).returns(List.empty)
      (requisitionsParser.getRequisitionsData _).when(requisitionsPage).returns(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.teamsCount _).when(*, *).onCall((_: String, page: String) => Success(page.toInt))

      provider.data shouldEqual ParsedData(Set.empty, Success(Set(requisitionData2, requisitionData3)))
    }

    "should filter failed requisitions" in {
      val teamPage = "team page"
      val requisitionsPage = "requisitions page"
      (connector.getTeamPage _).when().returns(teamPage)
      (connector.getRequisitionPage _).when().returns(requisitionsPage)
      (connector.getTournamentRequisitionsPage _).when(*).onCall((i: Int) => i.toString)

      val requisitionData1 = RequisitionData("test tournament", 1, "test agent 1", LocalDateTime.now())
      val requisitionData2 = RequisitionData("test tournament", 2, "test agent 2", LocalDateTime.now())
      val requisitionData3 = RequisitionData("test tournament", 3, "test agent 3", LocalDateTime.now())
      (csvParser.getTournamentsData _).when(teamPage).returns(List.empty)
      (requisitionsParser.getRequisitionsData _).when(requisitionsPage).returns(Success(List(requisitionData1, requisitionData2, requisitionData3)))
      (requisitionsPageParser.teamsCount _).when(*, *).onCall(
        (_: String, page: String) =>
          if (page.toInt % 2 == 0) Success(2) else Failure(new RuntimeException)
      )

      provider.data shouldEqual ParsedData(Set.empty, Success(Set(requisitionData2)))
    }
  }
}
