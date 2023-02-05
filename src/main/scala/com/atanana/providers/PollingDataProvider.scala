package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.ParsedData

import scala.concurrent.Future

trait PollingDataProvider {

  def data: EitherT[Future, Throwable, ParsedData]
}
