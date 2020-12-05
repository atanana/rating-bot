package com.atanana.processors

import javax.inject.Inject
import scala.concurrent.Future

class CommandProcessor @Inject()(pollProcessor: PollProcessor,
                                 reminderProcessor: ReminderProcessor,
                                 teamPositionsProcessor: TeamPositionsProcessor) {
  private val processors = Map(
    "poll" -> pollProcessor,
    "remind" -> reminderProcessor,
    "teamPositions" -> teamPositionsProcessor
  )

  def processCommand(command: String): Future[Either[String, Unit]] = {
    val processor = processors.getOrElse(command, throw new RuntimeException(s"Unknown command $command!"))
    processor.process()
  }
}

trait Processor {
  def process(): Future[Either[String, Unit]]
}