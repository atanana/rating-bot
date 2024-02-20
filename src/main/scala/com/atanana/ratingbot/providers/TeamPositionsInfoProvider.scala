package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.data.TeamPositionsInfo

import scala.concurrent.Future

trait TeamPositionsInfoProvider {

  def data: EitherT[Future, Throwable, TeamPositionsInfo]
}
