package com.atanana.posters

import cats.data.EitherT

import scala.concurrent.Future

trait Poster {

  def post(message: String): Either[String, Unit]

  def postAsync(message: String): EitherT[Future, Throwable, Unit]
}
