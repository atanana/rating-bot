package com.atanana

import cats.data.EitherT
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.data.*
import com.atanana.mocks.{MockMessageComposer, MockPoster, MockTournamentInfoProvider}
import com.atanana.posters.Poster
import com.atanana.providers.TournamentInfoProviderImpl
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.chaining.scalaUtilChainingOps

class CheckResultHandlerTest extends AnyWordSpecLike with Matchers with MockFactory {

  private val tournamentData = TournamentData(1, "test", "test link", 1.0f, 1, 12)
  private val changedTournament = ChangedTournament(tournamentData, 13)
  private val messageComposer = new MockMessageComposer()
  private val poster = new MockPoster()
  private val tournamentInfoProvider = new MockTournamentInfoProvider()

  "CheckResultHandler" should {

    "post all changes" in {
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      val editor = Editor("test editor")
      tournamentInfoProvider.editors.put(1, EitherT.rightT(List(editor)))

      messageComposer.newResultMessages.put(tournamentData, "new result")
      messageComposer.changedResultMessages.put(changedTournament, "changed result")
      messageComposer.newRequisitionMessages.put((requisitionData.toRequisition, List(editor)), "new requisition")
      messageComposer.cancelledRequisitionMessages.put(requisitionData.toRequisition, "cancelled requisition")

      poster.responses.put("new result", EitherT.rightT(()))
      poster.responses.put("changed result", EitherT.rightT(()))
      poster.responses.put("new requisition", EitherT.rightT(()))
      poster.responses.put("cancelled requisition", EitherT.rightT(()))

      val checkResult = CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData))
      )
      new CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(checkResult).pipe(awaitEither)

      poster.responses shouldBe empty
    }

    "return an error when cannot get editors" in {
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())

      tournamentInfoProvider.editors.put(1, EitherT.leftT(new RuntimeException("editors error")))

      new CheckResultHandler(poster, messageComposer, tournamentInfoProvider).processCheckResult(CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament)),
        RequisitionsCheckResult(Set(requisitionData), Set(requisitionData))
      )).pipe(awaitError) should have message "editors error"
    }
  }
}
