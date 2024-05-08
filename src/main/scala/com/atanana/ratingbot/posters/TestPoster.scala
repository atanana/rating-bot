package com.atanana.ratingbot.posters

import cats.data.EitherT
import cats.effect.IO

class TestPoster extends Poster {

  override def postAsync(message: String): EitherT[IO, Throwable, Unit] =
    EitherT.right(IO.println(message))
}
