package com.atanana.processors

import javax.inject.Inject

class CommandProcessor @Inject()(pollProcessor: PollProcessor, reminderProcessor: ReminderProcessor) {
  private val processors = Map(
    "poll" -> pollProcessor,
    "remind" -> reminderProcessor
  )

  def processCommand(command: String): Unit = {
    val processor = processors.getOrElse(command, throw new RuntimeException(s"Unknown command $command!"))
    processor.process()
  }
}

trait Processor {
  def process()
}