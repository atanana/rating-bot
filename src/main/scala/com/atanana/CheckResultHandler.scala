package com.atanana

import com.atanana.data.CheckResult

class CheckResultHandler(poster: Poster, messageComposer: MessageComposer) {
  def processCheckResult(checkResult: CheckResult): Unit = {
    List(
      checkResult.tournamentsCheckResult.newTournaments.map(messageComposer.composeNewResult),
      checkResult.tournamentsCheckResult.changedTournaments.map(messageComposer.composeChangedResult),
      checkResult.requisitionsCheckResult.newRequisitions.map(messageComposer.composeNewRequisition),
      checkResult.requisitionsCheckResult.cancelledRequisitions.map(messageComposer.composeCancelledRequisition)
    )
      .flatten
      .foreach(poster.post)
  }
}

object CheckResultHandler {
  def apply(poster: Poster, messageComposer: MessageComposer): CheckResultHandler = new CheckResultHandler(poster, messageComposer)
}