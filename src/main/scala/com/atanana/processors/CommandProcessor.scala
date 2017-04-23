package com.atanana.processors

import javax.inject.Inject

class CommandProcessor @Inject()(pollProcessor: PollProcessor) {
  private val processors = Map(
    "poll" -> pollProcessor
  )

  def processCommand(command: String): Unit = {
    val processor = processors.getOrElse(command, throw new RuntimeException(s"Unknown command $command!"))
    processor.process()
  }
}

trait Processor {
  def process()
}