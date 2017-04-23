package com.atanana.processors

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

class CommandProcessorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var pollProcessor: PollProcessor = _
  var reminderProcessor: ReminderProcessor = _
  var commandProcessor: CommandProcessor = _

  before {
    pollProcessor = mock[PollProcessor]
    reminderProcessor = mock[ReminderProcessor]
    commandProcessor = new CommandProcessor(pollProcessor, reminderProcessor)
  }

  "CommandProcessor" should {
    "dispatch command to poll processor" in {
      (pollProcessor.process _).expects()
      commandProcessor.processCommand("poll")
    }

    "dispatch command to remind processor" in {
      (reminderProcessor.process _).expects()
      commandProcessor.processCommand("remind")
    }

    "throw an error while processing unknown command" in {
      an[RuntimeException] should be thrownBy commandProcessor.processCommand("unknown")
    }
  }
}
