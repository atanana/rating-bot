package com.atanana.providers

import cats.data.EitherT

import scala.concurrent.Future

trait ReleasesProvider {

  def getLastReleaseId: EitherT[Future, Throwable, Int]
}
