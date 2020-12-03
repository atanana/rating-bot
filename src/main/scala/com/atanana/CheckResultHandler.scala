package com.atanana

import com.atanana.data.{CheckResult, RequisitionData}
import com.atanana.posters.Poster
import com.atanana.providers.TournamentInfoProvider
import com.atanana.utils.CollectionsUtils.eitherSet
import javax.inject.Inject

class CheckResultHandler @Inject()(
                                    poster: Poster,
                                    messageComposer: MessageComposer,
                                    tournamentInfoProvider: TournamentInfoProvider
                                  ) {

  def processCheckResult(checkResult: CheckResult): Either[String, Unit] =
    composeMessages(checkResult).map(_.foreach(poster.post))

  private def composeMessages(checkResult: CheckResult): Either[String, List[String]] = {
    val tournaments = checkResult.tournamentsCheckResult
    val requisitions = checkResult.requisitionsCheckResult
    for {
      newRequisitionsMessages <- eitherSet(requisitions.newRequisitions.map(getNewRequisitionMessage))
      newTournamentsMessages = tournaments.newTournaments.map(messageComposer.composeNewResult)
      changedTournamentsMessages = tournaments.changedTournaments.map(messageComposer.composeChangedResult)
      cancelledRequisitionsMessages = requisitions.cancelledRequisitions.map(messageComposer.composeCancelledRequisition)
    } yield List(newTournamentsMessages, changedTournamentsMessages, newRequisitionsMessages, cancelledRequisitionsMessages).flatten
  }

  private def getNewRequisitionMessage(newRequisition: RequisitionData) =
    tournamentInfoProvider.getEditors(newRequisition.tournamentId)
      .map(messageComposer.composeNewRequisition(newRequisition, _))
}