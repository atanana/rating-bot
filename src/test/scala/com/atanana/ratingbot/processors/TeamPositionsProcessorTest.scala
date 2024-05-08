package com.atanana.ratingbot.processors

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.*
import com.atanana.ratingbot.MessageComposer
import com.atanana.ratingbot.TestUtils.{getResult, getResultErrorMessage}
import com.atanana.ratingbot.data.{TargetTeam, TeamPositionsInfo}
import com.atanana.ratingbot.mocks.{MockMessageComposer, MockPoster, MockTeamPositionsInfoProvider}
import com.atanana.ratingbot.posters.Poster
import com.atanana.ratingbot.processors.TeamPositionsProcessorImpl
import com.atanana.ratingbot.providers.TeamPositionsInfoProviderImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TeamPositionsProcessorTest extends AnyWordSpecLike with Matchers {
  private val provider = new MockTeamPositionsInfoProvider()
  private val messageComposer = new MockMessageComposer()
  private val poster = new MockPoster()
  private val processor = new TeamPositionsProcessorImpl(provider, messageComposer, poster)

  "TeamPositionsProcessor" should {

    "post correct message" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam.some, targetTeam.some, targetTeam, 123, 200, 3000, 20, 30)
      provider.result = EitherT.rightT[IO, Throwable](info)
      messageComposer.teamPositionsMessage.put(info, "test message")
      poster.responses.put("test message", EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
      poster.responses shouldBe empty
    }

    "pass error from provider" in {
      provider.result = EitherT.leftT[IO, TeamPositionsInfo](new RuntimeException("123"))

      getResultErrorMessage(processor) shouldEqual "123"
    }

    "pass error from poster" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam.some, targetTeam.some, targetTeam, 123, 200, 3000, 20, 30)
      provider.result = EitherT.rightT[IO, Throwable](info)
      messageComposer.teamPositionsMessage.put(info, "test message")
      poster.responses.put("test message", EitherT.leftT(new RuntimeException("123")))

      getResultErrorMessage(processor) shouldEqual "123"
    }
  }
}
