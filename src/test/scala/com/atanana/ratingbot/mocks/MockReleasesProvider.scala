package com.atanana.ratingbot.mocks

import cats.data.EitherT
import com.atanana.ratingbot.providers.ReleasesProvider
import com.atanana.ratingbot.types.Ids.ReleaseId

import scala.concurrent.Future

class MockReleasesProvider extends ReleasesProvider {

  var releaseId: EitherT[Future, Throwable, ReleaseId] = _

  override def getLastReleaseId: EitherT[Future, Throwable, ReleaseId] = releaseId
}
