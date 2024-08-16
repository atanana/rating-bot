package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.{IO, Resource}
import com.typesafe.scalalogging.Logger

import scala.io.Source

class CommandProvider(pipe: String) {

  private val logger = Logger("CommandProvider")

  def getCommands: EitherT[IO, Throwable, List[String]] =
    EitherT {
      Resource.fromAutoCloseable {
          IO(Source.fromFile(pipe))
        }
        .evalMap(source => IO(source.getLines().toList))
        .attempt
        .use(IO.pure)
    }
      .leftSemiflatTap(error => IO(logger.error(s"Error while getting commands", error)))
}
