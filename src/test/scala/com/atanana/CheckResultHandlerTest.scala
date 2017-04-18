package com.atanana

import java.time.LocalDateTime

import com.atanana.data._
import com.atanana.providers.TournamentInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class CheckResultHandlerTest extends WordSpecLike with Matchers with MockFactory {
  "CheckResultHandler" should {
    "post all changes" in {
      val tournamentData = mock[TournamentData]
      val changedTournament = mock[ChangedTournament]
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      val tournamentInfoProvider = stub[TournamentInfoProvider]
      val editor = mock[Editor]
      (tournamentInfoProvider.getEditors _).when(1).returns(List(editor))

      val messageComposer = stub[MessageComposer]
      (messageComposer.composeNewResult _).when(tournamentData).returns("new result")
      (messageComposer.composeChangedResult _).when(changedTournament).returns("changed result")
      (messageComposer.composeNewRequisition _).when(requisitionData.toRequisition, List(editor)).returns("new requisition")
      (messageComposer.composeCancelledRequisition _).when(requisitionData.toRequisition).returns("cancelled requisition")

      val poster = mock[Poster]
      (poster.post _).expects("new result")
      (poster.post _).expects("changed result")
      (poster.post _).expects("new requisition")
      (poster.post _).expects("cancelled requisition")

      CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData.toRequisition))
      ))
    }
  }
}
