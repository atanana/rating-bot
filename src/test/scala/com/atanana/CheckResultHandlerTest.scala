package com.atanana

import java.time.LocalDateTime

import com.atanana.data._
import com.atanana.posters.Poster
import com.atanana.providers.TournamentInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class CheckResultHandlerTest extends AnyWordSpecLike with Matchers with MockFactory {

  private val tournamentData = TournamentData(1, "test", "test link", 1.0f, 1, 12)
  private val changedTournament = ChangedTournament(tournamentData, 13)
  private val messageComposer = stub[MessageComposer]
  private val poster = mock[Poster]
  private val tournamentInfoProvider = stub[TournamentInfoProvider]

  "CheckResultHandler" should {

    "post all changes" in {
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      val editor = mock[Editor]
      (tournamentInfoProvider.getEditors _).when(1).returns(Right(List(editor)))

      (messageComposer.composeNewResult _).when(tournamentData).returns("new result")
      (messageComposer.composeChangedResult _).when(changedTournament).returns("changed result")
      (messageComposer.composeNewRequisition _).when(requisitionData.toRequisition, List(editor)).returns("new requisition")
      (messageComposer.composeCancelledRequisition _).when(requisitionData.toRequisition).returns("cancelled requisition")

      (poster.post _).expects("new result")
      (poster.post _).expects("changed result")
      (poster.post _).expects("new requisition")
      (poster.post _).expects("cancelled requisition")

      new CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData))
      ))
    }

    "return an error when cannot get editors" in {
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      (tournamentInfoProvider.getEditors _).when(1).returns(Left("editors error"))

      new CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData))
      )) shouldEqual Left("editors error")
    }
  }
}
