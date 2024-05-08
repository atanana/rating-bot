package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.types.Ids.ReleaseId

trait ReleasesProvider {

  def getLastReleaseId: EitherT[IO, Throwable, ReleaseId]
}
