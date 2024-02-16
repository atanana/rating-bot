package com.atanana.providers

import cats.data.EitherT
import com.atanana.types.Ids.ReleaseId

import scala.concurrent.Future

trait ReleasesProvider {

  def getLastReleaseId: EitherT[Future, Throwable, ReleaseId]
}
