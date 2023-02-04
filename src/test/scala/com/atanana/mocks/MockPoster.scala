package com.atanana.mocks

import cats.data.EitherT
import com.atanana.posters.Poster

import scala.concurrent.Future

class MockPoster extends Poster {

  var message: String = _

  var response: EitherT[Future, Throwable, Unit] = _

  override def postAsync(message: String): EitherT[Future, Throwable, Unit] = {
    this.message = message
    response
  }
}
