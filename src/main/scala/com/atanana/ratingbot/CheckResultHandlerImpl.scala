package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.*
import com.atanana.ratingbot.data.{ChangedTournament, CheckResult, RequisitionData, TournamentResult}
import com.atanana.ratingbot.posters.Poster
import com.atanana.ratingbot.providers.TournamentInfoProvider
import com.atanana.ratingbot.{CheckResultHandler, MessageComposer}

class CheckResultHandlerImpl(
                              poster: Poster,
                              messageComposer: MessageComposer,
                              tournamentInfoProvider: TournamentInfoProvider
                            ) extends CheckResultHandler {

  override def processCheckResult(checkResult: CheckResult): EitherT[IO, Throwable, Unit] =
    for
      messages <- composeMessages(checkResult)
      _ <- messages.map(poster.postAsync).sequence
    yield ()

  private def composeMessages(checkResult: CheckResult): EitherT[IO, Throwable, List[String]] = {
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