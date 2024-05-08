package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.TeamPositionsInfo
import com.atanana.ratingbot.providers.TeamPositionsInfoProvider

import scala.collection.mutable

class MockTeamPositionsInfoProvider extends TeamPositionsInfoProvider {

  var result: EitherT[IO, Throwable, TeamPositionsInfo] = _

  override def data: EitherT[IO, Throwable, TeamPositionsInfo] = result
}
