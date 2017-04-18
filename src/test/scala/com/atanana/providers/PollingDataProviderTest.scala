package com.atanana.providers

import com.atanana.Connector
import com.atanana.data.{ParsedData, RequisitionData, TournamentData}
import com.atanana.parsers.{CsvParser, RequisitionsParser}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class PollingDataProviderTest extends WordSpecLike with MockFactory with Matchers {
  "PollingDataProvider" should {
    "provider valid data" in {
      val connector = stub[Connector]
      val csvParser = stub[CsvParser]
      val requisitionsParser = stub[RequisitionsParser]
      val provider = new PollingDataProvider(connector, csvParser, requisitionsParser)

      val teamPage = "team page"
      val requisitionsPage = "requisitions page"
      (connector.getTeamPage _).when().returns(teamPage)
      (connector.getRequisitionPage _).when().returns(requisitionsPage)

      val tournamentData = mock[TournamentData]
      val requisitionData = mock[RequisitionData]
      (csvParser.getTournamentsData _).when(teamPage).returns(List(tournamentData))
      (requisitionsParser.getRequisitionsData _).when(requisitionsPage).returns(List(requisitionData))

      provider.data shouldEqual ParsedData(Set(tournamentData), Set(requisitionData))
    }
  }
}
