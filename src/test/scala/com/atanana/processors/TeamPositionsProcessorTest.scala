package com.atanana.processors

import com.atanana.MessageComposer
import com.atanana.data.{TargetTeam, TeamPositionsInfo}
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TeamPositionsProcessorTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val provider = stub[TeamPositionsInfoProvider]
  private val messageComposer = stub[MessageComposer]
  private val poster = mock[Poster]
  private val processor = new TeamPositionsProcessor(provider, messageComposer, poster)

  "TeamPositionsProcessor" should {

    "post correct message" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam, targetTeam, targetTeam, 123, 200, 3000, 20, 30)
      (provider.data _).when().returns(Right(info))
      (messageComposer.composeTeamPositionsMessage _).when(info).returns("test message")
      (poster.post _).expects("test message") returns Right()

      processor.process() shouldEqual Right()
    }

    "pass error from provider" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      (provider.data _).when().returns(Left("123"))

      processor.process() shouldEqual Left("123")
    }

    "pass error from poster" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam, targetTeam, targetTeam, 123, 200, 3000, 20, 30)
      (provider.data _).when().returns(Right(info))
      (messageComposer.composeTeamPositionsMessage _).when(info).returns("test message")
      (poster.post _).expects("test message") returns Left("123")

      processor.process() shouldEqual Left("123")
    }
  }
}
