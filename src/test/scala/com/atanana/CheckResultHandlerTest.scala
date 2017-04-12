package com.atanana

import com.atanana.data._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class CheckResultHandlerTest extends WordSpecLike with Matchers with MockFactory {
  "CheckResultHandler" should {
    "post all changes" in {
      val tournamentData = mock[TournamentData]
      val changedTournament = mock[ChangedTournament]
      val requisition = mock[Requisition]

      val messageComposer = stub[MessageComposer]
      (messageComposer.composeNewResult _).when(tournamentData).returns("new result")
      (messageComposer.composeChangedResult _).when(changedTournament).returns("changed result")
      (messageComposer.composeNewRequisition _).when(requisition).returns("new requisition")
      (messageComposer.composeCancelledRequisition _).when(requisition).returns("cancelled requisition")

      val poster = mock[Poster]
      (poster.post _).expects("new result")
      (poster.post _).expects("changed result")
      (poster.post _).expects("new requisition")
      (poster.post _).expects("cancelled requisition")

      CheckResultHandler(poster, messageComposer).processCheckResult(CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisition), Set(requisition))
      ))
    }
  }
}
