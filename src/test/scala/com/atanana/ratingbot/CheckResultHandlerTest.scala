package com.atanana.ratingbot

import cats.data.EitherT
import com.atanana.ratingbot.CheckResultHandlerImpl
import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.TestUtils.{awaitEither, awaitError}
import com.atanana.ratingbot.data.*
import com.atanana.ratingbot.mocks.{MockMessageComposer, MockPoster, MockTournamentInfoProvider}
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.chaining.scalaUtilChainingOps

class CheckResultHandlerTest extends AnyWordSpecLike with Matchers {

  private val tournamentData = TournamentResult(1, 12, 1.0f, 1)
  private val changedTournament = ChangedTournament(tournamentData, 13)
  private val messageComposer = new MockMessageComposer()
  private val poster = new MockPoster()
  private val tournamentInfoProvider = new MockTournamentInfoProvider()

  "CheckResultHandler" should {

    "post all changes" in {
      tournamentInfoProvider.tournamentInfos(1) = EitherT.rightT(TournamentInfo("test", 36))

      messageComposer.newResultMessages.put(tournamentData, "new result")
      messageComposer.changedResultMessages.put(changedTournament, "changed result")

      poster.responses.put("new result", EitherT.rightT(()))
      poster.responses.put("changed result", EitherT.rightT(()))

      val checkResult = CheckResult(
        TournamentsCheckResult(Set(tournamentData), Set(changedTournament))
      )
      new CheckResultHandlerImpl(poster, messageComposer, tournamentInfoProvider).processCheckResult(checkResult).pipe(awaitEither)

      poster.responses shouldBe empty
    }
  }
}
