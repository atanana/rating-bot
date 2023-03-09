package com.atanana.processors

import cats.data.EitherT
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CommandProcessor(pollProcessor: PollProcessor,
                       reminderProcessor: ReminderProcessor,
                       teamPositionsProcessor: TeamPositionsProcessor) {

  private val logger = Logger(classOf[CommandProcessor])

  private val processors = Map(
    "poll" -> pollProcessor,
    "remind" -> reminderProcessor,
    "teamPositions" -> teamPositionsProcessor
  )

  def processCommand(command: String): EitherT[Future, Throwable, Unit] = {
    val processor = processors.getOrElse(command, createDefaultProcessor(command))
    processor.process()
  }

  private def createDefaultProcessor(command: String): Processor = () => {
    logger.warn(s"Unknown command $command!")
    EitherT.rightT(())
  }
}

trait Processor {
  def process(): EitherT[Future, Throwable, Unit]
}