package com.atanana.processors

import com.atanana.mocks.MockProcessor
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class CommandProcessorTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {
  private val pollProcessor = new MockProcessor()
  private val reminderProcessor = new MockProcessor()
  private val teamPositionsProcessor = new MockProcessor()
  private val commandProcessor = new CommandProcessor(pollProcessor, reminderProcessor, teamPositionsProcessor)

  after {
    pollProcessor.reset()
    reminderProcessor.reset()
    teamPositionsProcessor.reset()
  }

  "CommandProcessor" should {
    "dispatch command to poll processor" in {
      commandProcessor.processCommand("poll")
      pollProcessor.invocationsCount shouldEqual 1
      reminderProcessor.invocationsCount shouldEqual 0
      teamPositionsProcessor.invocationsCount shouldEqual 0
    }

    "dispatch command to remind processor" in {
      commandProcessor.processCommand("remind")
      pollProcessor.invocationsCount shouldEqual 0
      reminderProcessor.invocationsCount shouldEqual 1
      teamPositionsProcessor.invocationsCount shouldEqual 0
    }

    "dispatch command to team positions processor" in {
      commandProcessor.processCommand("teamPositions")
      pollProcessor.invocationsCount shouldEqual 0
      reminderProcessor.invocationsCount shouldEqual 0
      teamPositionsProcessor.invocationsCount shouldEqual 1
    }

    "throw an error while processing unknown command" in {
      an[RuntimeException] should be thrownBy commandProcessor.processCommand("unknown")
    }
  }
}
