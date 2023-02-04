package com.atanana.mocks

import cats.data.EitherT
import com.atanana.data.ParsedData
import com.atanana.providers.PollingDataProvider

import scala.concurrent.Future

class MockPollingDataProvider extends PollingDataProvider {

  var result: EitherT[Future, Throwable, ParsedData] = _

  override def data: EitherT[Future, Throwable, ParsedData] = result
}
