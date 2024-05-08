package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.ParsedData
import com.atanana.ratingbot.providers.PollingDataProvider

class MockPollingDataProvider extends PollingDataProvider {

  var result: EitherT[IO, Throwable, ParsedData] = _

  override def data: EitherT[IO, Throwable, ParsedData] = result
}
