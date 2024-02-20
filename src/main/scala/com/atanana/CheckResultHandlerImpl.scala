package com.atanana

import cats.data.EitherT
import cats.implicits.*
import com.atanana.data.{ChangedTournament, CheckResult, RequisitionData, TournamentResult}
import com.atanana.posters.Poster
import com.atanana.providers.TournamentInfoProvider

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CheckResultHandlerImpl(
                              poster: Poster,
                              messageComposer: MessageComposer,
                              tournamentInfoProvider: TournamentInfoProvider
                            ) extends CheckResultHandler {

  override def processCheckResult(checkResult: CheckResult): EitherT[Future, Throwable, Unit] =
    for
      messages <- composeMessages(checkResult)
      _ <- messages.map(poster.postAsync).sequence
    yield ()

  private def composeMessages(checkResult: CheckResult): EitherT[Future, Throwable, List[String]] = {
    val tournaments = checkResult.tournamentsCheckResult
    val requisitions = checkResult.requisitionsCheckResult
    for
      newRequisitionsMessages <- requisitions.newRequisitions.map(getNewRequisitionMessage).toList.sequence
      newTournamentsMessages <- tournaments.newTournaments.map(getNewTournamentResultMessage).toList.sequence
      changedTournamentsMessages <- tournaments.changedTournaments.map(getChangedTournamentResultMessage).toList.sequence
      cancelledRequisitionsMessages = requisitions.cancelledRequisitions.map(messageComposer.composeCancelledRequisition).toList
    yield List(newTournamentsMessages, changedTournamentsMessages, newRequisitionsMessages, cancelledRequisitionsMessages).flatten
  }

  private def getNewRequisitionMessage(newRequisition: RequisitionData) = for
    editors <- tournamentInfoProvider.getEditors(newRequisition.tournamentId)
  yield messageComposer.composeNewRequisition(newRequisition, editors)

  private def getNewTournamentResultMessage(result: TournamentResult) = for
    tournamentInfo <- tournamentInfoProvider.getInfo(result.id)
  yield messageComposer.composeNewResult(result, tournamentInfo)

  private def getChangedTournamentResultMessage(tournament: ChangedTournament) = for
    tournamentInfo <- tournamentInfoProvider.getInfo(tournament.tournament.id)
  yield messageComposer.composeChangedResult(tournament, tournamentInfo)
}