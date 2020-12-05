package com.atanana.posters
import scala.concurrent.Future

class TestPoster extends Poster {

  override def post(message: String): Either[String, Unit] = {
    println(message)
    Right()
  }

  override def postAsync(message: String): Future[Either[String, Unit]] = Future.successful(post(message))
}
