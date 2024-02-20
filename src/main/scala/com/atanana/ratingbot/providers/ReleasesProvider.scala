package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.types.Ids.ReleaseId

import scala.concurrent.Future

trait ReleasesProvider {

  def getLastReleaseId: EitherT[Future, Throwable, ReleaseId]
}
