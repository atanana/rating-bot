package com.atanana.providers

import javax.inject.Inject

import com.atanana.Connector
import com.atanana.data.ParsedData
import com.atanana.parsers.{CsvParser, RequisitionsParser}

class PollingDataProvider @Inject()(connector: Connector, csvParser: CsvParser, requisitionsParser: RequisitionsParser) {
  def data: ParsedData = {
    ParsedData(
      getNewTournaments,
      getNewRequisitions
    )
  }

  private def getNewRequisitions = {
    val requisitionPage = connector.getRequisitionPage
    requisitionsParser.getRequisitionsData(requisitionPage).toSet
  }

  private def getNewTournaments = {
    val teamCsv = connector.getTeamPage
    csvParser.getTournamentsData(teamCsv).toSet
  }
}
