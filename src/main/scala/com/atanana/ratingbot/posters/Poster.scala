package com.atanana.ratingbot.posters

import cats.data.EitherT
import cats.effect.IO

trait Poster {

  def postAsync(message: String): EitherT[IO, Throwable, Unit]
}
