package com.atanana.providers

import cats.data.EitherT
import cats.implicits.*
import com.atanana.data.{ParsedData, PartialRequisitionData, RequisitionData, TournamentData}
import com.atanana.json.Config
import com.atanana.net.{Connector, ConnectorImpl}
import com.atanana.parsers.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PollingDataProviderImpl(
                               connector: Connector,
                               csvParser: CsvParser,
                               requisitionsParser: RequisitionsParser,
                               requisitionsPageParser: RequisitionsPageParser,
                               tournamentInfoParser: TournamentInfoParser,
                               config: Config
                             ) extends PollingDataProvider {

  override def data: EitherT[Future, Throwable, ParsedData] =
    for
      newTournaments <- getNewTournaments
      newRequisitions <- getNewRequisitions
    yield ParsedData(newTournaments, newRequisitions)

  private def getNewRequisitions: EitherT[Future, Throwable, Set[RequisitionData]] =
    for
      requisitionsPage <- connector.getRequisitionPage
      requisitions <- EitherT.fromEither[Future](requisitionsParser.getRequisitionsData(requisitionsPage).toEither)
      filteredRequisitions <- filterRequisitions(requisitions.toSet)
      results <- addQuestionsCount(filteredRequisitions)
    yield results

  private def filterRequisitions(requisitions: Set[PartialRequisitionData]): EitherT[Future, Throwable, Set[PartialRequisitionData]] = {
    for
      results <- requisitions.toList.traverse(requisition =>
        checkRequisitionAsync(requisition).map((_, requisition))
      ).map(_.toSet)
    yield for
      (checkResult, requisition) <- results
      if checkResult
    yield requisition
  }

  private def checkRequisitionAsync(requisition: PartialRequisitionData): EitherT[Future, Throwable, Boolean] =
    for
      requisitionsPage <- connector.getTournamentRequisitionsPage(requisition.tournamentId)
      data <- EitherT.fromEither[Future](requisitionsPageParser.additionalData(requisition.agent, requisitionsPage).toEither)
    yield data.teamsCount > 1 && !config.ignoredVenues.contains(data.venue)

  private def addQuestionsCount(requisitions: Set[PartialRequisitionData]): EitherT[Future, Throwable, Set[RequisitionData]] =
    requisitions.toList.traverse(requisition =>
      getQuestionCount(requisition.tournamentId).map(requisition.toRequisitionData)
    ).map(_.toSet)

  private def getNewTournaments: EitherT[Future, Throwable, Set[TournamentData]] =
    for
      page <- connector.getTeamPage
    yield csvParser.getTournamentsData(page).toSet

  private def getQuestionCount(tournamentId: Int): EitherT[Future, Throwable, Int] =
    for
      tournamentInfo <- connector.getTournamentInfo(tournamentId)
      questionsCount <- EitherT.fromEither[Future](tournamentInfoParser.getQuestionsCount(tournamentInfo).toEither)
    yield questionsCount
}
