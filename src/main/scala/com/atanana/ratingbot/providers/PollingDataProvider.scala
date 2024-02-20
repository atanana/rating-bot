package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.data.ParsedData

import scala.concurrent.Future

trait PollingDataProvider {

  def data: EitherT[Future, Throwable, ParsedData]
}
