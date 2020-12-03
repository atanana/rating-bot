package com.atanana.posters

trait Poster {

  def post(message: String): Either[String, Unit]
}
