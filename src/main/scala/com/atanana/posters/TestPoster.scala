package com.atanana.posters

class TestPoster extends Poster {
  override def post(message: String): Either[String, Unit] = {
    println(message)
    Right()
  }
}
