package com.atanana.ratingbot.mocks

import cats.data.EitherT
import com.atanana.ratingbot.data.ParsedData
import com.atanana.ratingbot.providers.PollingDataProvider

import scala.concurrent.Future

class MockPollingDataProvider extends PollingDataProvider {

  var result: EitherT[Future, Throwable, ParsedData] = _

  override def data: EitherT[Future, Throwable, ParsedData] = result
}
