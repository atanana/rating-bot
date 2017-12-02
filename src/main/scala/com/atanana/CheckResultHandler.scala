package com.atanana

import javax.inject.Inject

import com.atanana.data.{CheckResult, RequisitionData}
import com.atanana.posters.Poster
import com.atanana.providers.TournamentInfoProvider

class CheckResultHandler @Inject()(poster: Poster, messageComposer: MessageComposer, tournamentInfoProvider: TournamentInfoProvider) {
  def processCheckResult(checkResult: CheckResult): Unit = {
    List(
      checkResult.tournamentsCheckResult.newTournaments.map(messageComposer.composeNewResult),
      checkResult.tournamentsCheckResult.changedTournaments.map(messageComposer.composeChangedResult),
      checkResult.requisitionsCheckResult.newRequisitions.map(getNewRequisitionMessage),
      checkResult.requisitionsCheckResult.cancelledRequisitions.map(messageComposer.composeCancelledRequisition)
    )
      .flatten
      .foreach(poster.post)
  }

  private def getNewRequisitionMessage(newRequisition: RequisitionData) = {
    val editors = tournamentInfoProvider.getEditors(newRequisition.tournamentId)
    messageComposer.composeNewRequisition(newRequisition, editors)
  }
}

object CheckResultHandler {
  def apply(poster: Poster, messageComposer: MessageComposer, tournamentInfoProvider: TournamentInfoProvider): CheckResultHandler = new CheckResultHandler(poster, messageComposer, tournamentInfoProvider)
}