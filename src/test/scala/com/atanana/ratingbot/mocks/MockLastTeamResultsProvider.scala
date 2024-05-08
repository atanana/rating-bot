package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.providers.LastTeamResultsProvider
import com.atanana.ratingbot.types.Ids.TeamId

import scala.collection.mutable

class MockLastTeamResultsProvider extends LastTeamResultsProvider {

  val results: mutable.Map[TeamId, EitherT[IO, Throwable, Set[TournamentResult]]] = mutable.Map()

  override def getLastTeamResults(teamId: TeamId): EitherT[IO, Throwable, Set[TournamentResult]] = results(teamId)
}
