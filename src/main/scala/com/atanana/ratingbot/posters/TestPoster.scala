package com.atanana.ratingbot.posters

import cats.data.EitherT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TestPoster extends Poster {

  override def postAsync(message: String): EitherT[Future, Throwable, Unit] =
    EitherT.rightT(println(message))
}
