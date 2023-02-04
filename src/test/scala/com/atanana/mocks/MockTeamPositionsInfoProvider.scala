package com.atanana.mocks

import cats.data.EitherT
import com.atanana.data.TeamPositionsInfo
import com.atanana.providers.TeamPositionsInfoProvider

import scala.concurrent.Future
import scala.collection.mutable

class MockTeamPositionsInfoProvider extends TeamPositionsInfoProvider {

  var result: EitherT[Future, Throwable, TeamPositionsInfo] = _

  override def data: EitherT[Future, Throwable, TeamPositionsInfo] = result
}
