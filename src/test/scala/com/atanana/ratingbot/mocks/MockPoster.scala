package com.atanana.ratingbot.mocks

import cats.data.EitherT
import com.atanana.ratingbot.posters.Poster

import scala.collection.mutable
import scala.concurrent.Future

class MockPoster extends Poster {

  var responses: mutable.Map[String, EitherT[Future, Throwable, Unit]] = mutable.Map[String, EitherT[Future, Throwable, Unit]]()

  override def postAsync(message: String): EitherT[Future, Throwable, Unit] = responses.remove(message).get
}
