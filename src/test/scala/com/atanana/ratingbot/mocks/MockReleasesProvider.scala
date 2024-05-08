package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.providers.ReleasesProvider
import com.atanana.ratingbot.types.Ids.ReleaseId

class MockReleasesProvider extends ReleasesProvider {

  var releaseId: EitherT[IO, Throwable, ReleaseId] = _

  override def getLastReleaseId: EitherT[IO, Throwable, ReleaseId] = releaseId
}
