package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.*
import com.atanana.ratingbot.data.{ParsedData, PartialRequisitionData, RequisitionData}
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.net.Connector
import com.atanana.ratingbot.parsers.*
import com.atanana.ratingbot.types.Ids.{TeamId, TournamentId}
import com.typesafe.scalalogging.Logger

import scala.util.chaining.*

class PollingDataProviderImpl(
                               connector: Connector,
                               requisitionsParser: RequisitionsParser,
                               requisitionsPageParser: RequisitionsPageParser,
                               tournamentInfoProvider: TournamentInfoProvider,
                               lastTeamResultsProvider: LastTeamResultsProvider,
                               config: Config,
                             ) extends PollingDataProvider {

  private val logger = Logger("PollingDataProvider")

  override def data: EitherT[IO, Throwable, ParsedData] =
    for
      newTournaments <- lastTeamResultsProvider.getLastTeamResults(TeamId(config.team))
      newRequisitions <- getNewRequisitions
    yield ParsedData(newTournaments, newRequisitions).tap(data => logger.debug(s"Got data $data"))

  private def getNewRequisitions: EitherT[IO, Throwable, Set[RequisitionData]] =
    for
      requisitionsPage <- connector.getRequisitionPage
      requisitions <- EitherT.fromEither(requisitionsParser.getRequisitionsData(requisitionsPage).toEither)
      filteredRequisitions <- filterRequisitions(requisitions.toSet)
      results <- addQuestionsCount(filteredRequisitions)
    yield results

  private def filterRequisitions(requisitions: Set[PartialRequisitionData]): EitherT[IO, Throwable, Set[PartialRequisitionData]] = {
    requisitions.toList.traverseFilter(requisition => {
      checkRequisitionAsync(requisition).map(_.guard[Option].as(requisition))
        .recover(* => None) //todo don't swallow errors here
    }).map(_.toSet)
  }

  private def checkRequisitionAsync(requisition: PartialRequisitionData): EitherT[IO, Throwable, Boolean] =
    for
      requisitionsPage <- connector.getTournamentRequisitionsPage(requisition.tournamentId)
      data <- EitherT.fromEither(requisitionsPageParser.additionalData(requisition.agent, requisitionsPage).toEither)
    yield data.teamsCount > 1 && !config.ignoredVenues.contains(data.venue)

  private def addQuestionsCount(requisitions: Set[PartialRequisitionData]): EitherT[IO, Throwable, Set[RequisitionData]] =
    requisitions.toList.traverse(requisition =>
      getQuestionCount(requisition.tournamentId).map(requisition.toRequisitionData)
    ).map(_.toSet)

  private def getQuestionCount(tournamentId: TournamentId): EitherT[IO, Throwable, Int] =
    for
      tournamentInfo <- tournamentInfoProvider.getInfo(tournamentId)
    yield tournamentInfo.questionsCount
}
