package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.TeamPositionsInfo

trait TeamPositionsInfoProvider {

  def data: EitherT[IO, Throwable, TeamPositionsInfo]
}
