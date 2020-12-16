package com.atanana.posters

import cats.data.EitherT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TestPoster extends Poster {

  override def post(message: String): Either[String, Unit] = {
    println(message)
    Right()
  }

  override def postAsync(message: String): EitherT[Future, Throwable, Unit] = EitherT.fromEither[Future](post(message).left.map(new RuntimeException(_)))
}
