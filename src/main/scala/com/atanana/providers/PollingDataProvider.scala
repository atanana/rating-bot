package com.atanana.providers

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import com.atanana.Connector
import com.atanana.data.{ParsedData, RequisitionData}
import com.atanana.parsers.{CsvParser, RequisitionsPageParser, RequisitionsParser}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

class PollingDataProvider @Inject()(connector: Connector, csvParser: CsvParser, requisitionsParser: RequisitionsParser,
                                    requisitionsPageParser: RequisitionsPageParser) {
  def data: ParsedData = {
    ParsedData(
      getNewTournaments,
      getNewRequisitions
    )
  }

  private def getNewRequisitions: Try[Set[RequisitionData]] = {
    val requisitionPage = connector.getRequisitionPage
    requisitionsParser.getRequisitionsData(requisitionPage)
      .map(requisitions =>
        zipWithTeamsCount(requisitions.toSet)
          .filter({ case (_, teamsCount) => teamsCount > 1 })
          .map({ case (requisition, _) => requisition })
      )
  }

  private def zipWithTeamsCount(requisitions: Set[RequisitionData]) = {
    Await.result(Future.sequence(
      requisitions
        .map(requisition => Future {
          val requisitionsPage = connector.getTournamentRequisitionsPage(requisition.tournamentId)
          val teamsCount = requisitionsPageParser.teamsCount(requisition.agent, requisitionsPage).getOrElse(0)
          (requisition, teamsCount)
        })
    ), Duration(10, TimeUnit.MINUTES))
  }

  private def getNewTournaments = {
    val teamCsv = connector.getTeamPage
    csvParser.getTournamentsData(teamCsv).toSet
  }
}
