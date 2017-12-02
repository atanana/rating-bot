package com.atanana.processors

import com.atanana.MessageComposer
import com.atanana.data.{TargetTeam, TeamPositionsInfo}
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, WordSpecLike}

import scala.util.Success

class TeamPositionsProcessorTest extends WordSpecLike with MockFactory with BeforeAndAfter {
  var provider: TeamPositionsInfoProvider = _
  var messageComposer: MessageComposer = _
  var poster: Poster = _
  var processor: TeamPositionsProcessor = _

  before {
    provider = stub[TeamPositionsInfoProvider]
    messageComposer = stub[MessageComposer]
    poster = mock[Poster]
    processor = new TeamPositionsProcessor(provider, messageComposer, poster)
  }

  "TeamPositionsProcessor" should {
    "post correct message" in {
      val targetTeam = TargetTeam("test team", "test city", 100)
      val info = TeamPositionsInfo(targetTeam, targetTeam, targetTeam, 123, 200, 3000, 20, 30)
      (provider.data _).when().returns(Success(info))
      (messageComposer.composeTeamPositionsMessage _).when(info).returns("test message")
      (poster.post _).expects("test message")

      processor.process()
    }
  }
}
