package com.atanana.processors

import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

//class CommandProcessorTest extends AnyWordSpecLike with MockFactory with Matchers {
//  private val pollProcessor = mock[PollProcessor]
//  private val reminderProcessor = mock[ReminderProcessor]
//  private val teamPositionsProcessor = mock[TeamPositionsProcessor]
//  private val commandProcessor = new CommandProcessor(pollProcessor, reminderProcessor, teamPositionsProcessor)
//
//  "CommandProcessor" should {
//    "dispatch command to poll processor" in {
//      (pollProcessor.process _).expects()
//      commandProcessor.processCommand("poll")
//    }
//
//    "dispatch command to remind processor" in {
//      (reminderProcessor.process _).expects()
//      commandProcessor.processCommand("remind")
//    }
//
//    "dispatch command to team positions processor" in {
//      (teamPositionsProcessor.process _).expects()
//      commandProcessor.processCommand("teamPositions")
//    }
//
//    "throw an error while processing unknown command" in {
//      an[RuntimeException] should be thrownBy commandProcessor.processCommand("unknown")
//    }
//  }
//}
