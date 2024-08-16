package com.atanana.ratingbot.processors

import cats.data.EitherT
import cats.effect.IO
import com.typesafe.scalalogging.Logger

class CommandProcessor(pollProcessor: PollProcessor,
                       reminderProcessor: ReminderProcessor,
                       teamPositionsProcessor: TeamPositionsProcessor) {

  private val logger = Logger(classOf[CommandProcessor])

  private val processors = Map(
    "poll" -> pollProcessor,
    "remind" -> reminderProcessor,
    "teamPositions" -> teamPositionsProcessor
  )

  def processCommand(command: String): IO[Unit] = {
    logger.debug(s"Process command $command")
    val processor = processors.getOrElse(command, createDefaultProcessor(command))
    processor.process()
      .leftSemiflatTap(error => IO(logger.error(s"Error while processing command $command", error)))
      .value
      .map(* => ())
  }

  private def createDefaultProcessor(command: String): Processor = () => {
    logger.warn(s"Unknown command $command!")
    EitherT.rightT(())
  }
}

trait Processor {
  def process(): EitherT[IO, Throwable, Unit]
}