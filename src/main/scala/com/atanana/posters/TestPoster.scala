package com.atanana.posters

class TestPoster extends Poster {
  override def post(message: String): Unit = {
    println(message)
  }
}
