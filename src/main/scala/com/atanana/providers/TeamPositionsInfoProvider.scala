package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.TeamPositionsInfo

import scala.concurrent.Future

trait TeamPositionsInfoProvider {

  def data: EitherT[Future, Throwable, TeamPositionsInfo]
}
