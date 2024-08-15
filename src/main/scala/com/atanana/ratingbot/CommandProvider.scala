package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.{IO, Resource}
import com.typesafe.scalalogging.Logger

import scala.io.Source

class CommandProvider(pipe: String) {

  private val logger = Logger("CommandProvider")

  def getCommand: EitherT[IO, Throwable, String] =
    EitherT {
      Resource.fromAutoCloseable {
          IO(Source.fromFile(pipe))
        }
        .evalMap(source => IO(source.mkString.trim))
        .attempt
        .use(IO.pure)
    }
}
