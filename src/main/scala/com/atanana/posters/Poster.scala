package com.atanana.posters

import scala.concurrent.Future

trait Poster {

  def post(message: String): Either[String, Unit]

  def postAsync(message: String): Future[Either[String, Unit]]
}
