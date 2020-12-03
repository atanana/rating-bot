package com.atanana.processors

import javax.inject.Inject

class CommandProcessor @Inject()(pollProcessor: PollProcessor,
                                 reminderProcessor: ReminderProcessor,
                                 teamPositionsProcessor: TeamPositionsProcessor) {
  private val processors = Map(
    "poll" -> pollProcessor,
    "remind" -> reminderProcessor,
    "teamPositions" -> teamPositionsProcessor
  )

  def processCommand(command: String): Either[String, Unit] = {
    val processor = processors.getOrElse(command, throw new RuntimeException(s"Unknown command $command!"))
    processor.process()
  }
}

trait Processor {
  def process(): Either[String, Unit]
}