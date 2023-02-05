package com.atanana.processors

import cats.data.EitherT
import cats.implicits._
import com.atanana.MessageComposer
import com.atanana.TestUtils.{getResult, getResultErrorMessage}
import com.atanana.data.{TargetTeam, TeamPositionsInfo}
import com.atanana.mocks.{MockMessageComposer, MockPoster, MockTeamPositionsInfoProvider}
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProviderImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamPositionsProcessorTest extends AnyWordSpecLike with Matchers {
  private val provider = new MockTeamPositionsInfoProvider()
  private val messageComposer = new MockMessageComposer()
  private val poster = new MockPoster()
  private val processor = new TeamPositionsProcessorImpl(provider, messageComposer, poster)

  "TeamPositionsProcessor" should {

    "post correct message" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam.some, targetTeam.some, targetTeam, 123, 200, 3000, 20, 30)
      provider.result = EitherT.rightT[Future, Throwable](info)
      messageComposer.teamPositionsMessage.put(info, "test message")
      poster.responses.put("test message", EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
      poster.responses shouldBe empty
    }

    "pass error from provider" in {
      provider.result = EitherT.leftT[Future, TeamPositionsInfo](new RuntimeException("123"))

      getResultErrorMessage(processor) shouldEqual "123"
    }

    "pass error from poster" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam.some, targetTeam.some, targetTeam, 123, 200, 3000, 20, 30)
      provider.result = EitherT.rightT[Future, Throwable](info)
      messageComposer.teamPositionsMessage.put(info, "test message")
      poster.responses.put("test message", EitherT.leftT(new RuntimeException("123")))

      getResultErrorMessage(processor) shouldEqual "123"
    }
  }
}
