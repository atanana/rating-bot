package com.atanana.ratingbot.mocks

import cats.data.EitherT
import com.atanana.ratingbot.data.TeamPositionsInfo
import com.atanana.ratingbot.providers.TeamPositionsInfoProvider

import scala.collection.mutable
import scala.concurrent.Future

class MockTeamPositionsInfoProvider extends TeamPositionsInfoProvider {

  var result: EitherT[Future, Throwable, TeamPositionsInfo] = _

  override def data: EitherT[Future, Throwable, TeamPositionsInfo] = result
}
