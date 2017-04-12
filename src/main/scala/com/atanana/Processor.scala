package com.atanana

import com.atanana.checkers.MainChecker
import com.atanana.data.Data
import com.atanana.parsers.{CsvParser, RequisitionsParser}

class Processor(connector: Connector, csvParser: CsvParser, requisitionsParser: RequisitionsParser,
                store: JsonStore, checker: MainChecker, checkResultHandler: CheckResultHandler) {
  def process(): Unit = {
    val newTournaments = getNewTournaments
    val newRequisitions = getNewRequisitions
    val storedData = store.read

    val checkResult = checker.check(storedData, newTournaments, newRequisitions)
    checkResultHandler.processCheckResult(checkResult)

    val newData = Data(
      newTournaments.map(_.toTournament),
      newRequisitions
    )

    if (storedData != newData) {
      store.write(newData)
    }
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

object Processor {
  def apply(connector: Connector, csvParser: CsvParser, requisitionsParser: RequisitionsParser,
            jsonStore: JsonStore, mainChecker: MainChecker, checkResultHandler: CheckResultHandler) =
    new Processor(connector, csvParser, requisitionsParser, jsonStore, mainChecker, checkResultHandler)
}