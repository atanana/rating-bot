package com.atanana.mocks

import cats.data.EitherT
import com.atanana.providers.ReleasesProvider

import scala.concurrent.Future

class MockReleasesProvider extends ReleasesProvider {

  var releaseId: EitherT[Future, Throwable, Int] = _

  override def getLastReleaseId: EitherT[Future, Throwable, Int] = releaseId
}
