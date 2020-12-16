package com.atanana

import cats.data.EitherT
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.data._
import com.atanana.posters.Poster
import com.atanana.providers.TournamentInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.chaining.scalaUtilChainingOps

class CheckResultHandlerTest extends AnyWordSpecLike with Matchers with MockFactory {

  private val tournamentData = TournamentData(1, "test", "test link", 1.0f, 1, 12)
  private val changedTournament = ChangedTournament(tournamentData, 13)
  private val messageComposer = stub[MessageComposer]
  private val poster = mock[Poster]
  private val tournamentInfoProvider = stub[TournamentInfoProvider]

  "CheckResultHandler" should {

    "post all changes" in {
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      val editor = Editor("test editor")
      (tournamentInfoProvider.getEditors _).when(1) returns EitherT.rightT(List(editor))

      (messageComposer.composeNewResult _).when(tournamentData).returns("new result")
      (messageComposer.composeChangedResult _).when(changedTournament).returns("changed result")
      (messageComposer.composeNewRequisition _).when(requisitionData.toRequisition, List(editor)).returns("new requisition")
      (messageComposer.composeCancelledRequisition _).when(requisitionData.toRequisition).returns("cancelled requisition")

      (poster.postAsync _).expects("new result") returns EitherT.rightT(())
      (poster.postAsync _).expects("changed result") returns EitherT.rightT(())
      (poster.postAsync _).expects("new requisition") returns EitherT.rightT(())
      (poster.postAsync _).expects("cancelled requisition") returns EitherT.rightT(())

      val checkResult = CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData))
      )
      new CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(checkResult).pipe(awaitEither)
    }

    "return an error when cannot get editors" in {
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      (tournamentInfoProvider.getEditors _).when(1) returns EitherT.leftT(new RuntimeException("editors error"))

      new CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData))
      )).pipe(awaitError) should have message "editors error"
    }
  }
}
