package com.atanana.processors

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

class CommandProcessorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var pollProcessor: PollProcessor = _
  var commandProcessor: CommandProcessor = _

  before {
    pollProcessor = mock[PollProcessor]
    commandProcessor = new CommandProcessor(pollProcessor)
  }

  "CommandProcessor" should {
    "dispatch command to poll processor" in {
      (pollProcessor.process _).expects()
      commandProcessor.processCommand("poll")
    }

    "throw an error while processing unknown command" in {
      an[RuntimeException] should be thrownBy commandProcessor.processCommand("unknown")
    }
  }
}
