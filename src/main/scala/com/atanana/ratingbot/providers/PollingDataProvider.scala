package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.ParsedData

trait PollingDataProvider {

  def data: EitherT[IO, Throwable, ParsedData]
}
