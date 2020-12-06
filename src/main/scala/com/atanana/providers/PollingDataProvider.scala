package com.atanana.providers

import com.atanana.Connector
import com.atanana.data.{ParsedData, PartialRequisitionData, RequisitionData}
import com.atanana.json.Config
import com.atanana.parsers._

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

class PollingDataProvider @Inject()(
                                     connector: Connector,
                                     csvParser: CsvParser,
                                     requisitionsParser: RequisitionsParser,
                                     requisitionsPageParser: RequisitionsPageParser,
                                     tournamentInfoParser: TournamentInfoParser,
                                     config: Config
                                   ) {

  def data: Future[Either[String, ParsedData]] =
    for {
      newTournamentsEither <- getNewTournaments
    } yield for {
      newTournaments <- newTournamentsEither
      newRequisitions = getNewRequisitions
    } yield ParsedData(newTournaments, newRequisitions)

  //todo refactor
  private def getNewRequisitions: Try[Set[RequisitionData]] = {
    val requisitionPage = connector.getRequisitionPage.right.get
    requisitionsParser.getRequisitionsData(requisitionPage)
      .map(requisitions =>
        zipWithTeamsCount(requisitions.toSet)
          .filter({ case (_, additionalData) => checkRequisition(additionalData) })
          .map({ case (requisition, _) => requisition })
      )
      .map(addQuestionCount)
  }

  private def checkRequisition(additionalData: RequisitionAdditionalData) =
    additionalData.teamsCount > 1 && !config.ignoredVenues.contains(additionalData.venue)

  private def zipWithTeamsCount(requisitions: Set[PartialRequisitionData]) =
    Await.result(Future.sequence(
      requisitions
        .map(requisition => Future {
          val requisitionsPage = connector.getTournamentRequisitionsPage(requisition.tournamentId).right.get
          requisitionsPageParser.additionalData(requisition.agent, requisitionsPage)
            .map(data => (requisition, data))
        })
    ), Duration(10, TimeUnit.MINUTES))
      .flatMap(_.toOption.toList)

  private def addQuestionCount(requisitions: Set[PartialRequisitionData]): Set[RequisitionData] = {
    Await.result(Future.sequence(
      requisitions
        .map(requisition => Future {
          val requisitionsPage = connector.getTournamentInfo(requisition.tournamentId).right.get
          val questionsCount = tournamentInfoParser.getQuestionsCount(requisitionsPage).getOrElse(0)
          requisition.toRequisitionData(questionsCount)
        })
    ), Duration(10, TimeUnit.MINUTES))
  }

  private def getNewTournaments =
    for {
      pageEither <- connector.getTeamPage
    } yield for {
      page <- pageEither
      tournamentsData = csvParser.getTournamentsData(page)
    } yield tournamentsData.toSet
}
