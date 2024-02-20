package com.atanana.ratingbot.processors

import cats.data.EitherT
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
    logger.debug(s"Process command $command")
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