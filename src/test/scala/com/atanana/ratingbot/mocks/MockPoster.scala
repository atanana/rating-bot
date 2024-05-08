package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.posters.Poster

import scala.collection.mutable

class MockPoster extends Poster {

  var responses: mutable.Map[String, EitherT[IO, Throwable, Unit]] = mutable.Map[String, EitherT[IO, Throwable, Unit]]()

  override def postAsync(message: String): EitherT[IO, Throwable, Unit] = responses.remove(message).get
}
