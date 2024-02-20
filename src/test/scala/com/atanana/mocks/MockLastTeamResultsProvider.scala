package com.atanana.mocks

import cats.data.EitherT
import com.atanana.data.TournamentResult
import com.atanana.providers.LastTeamResultsProvider
import com.atanana.types.Ids.TeamId

import scala.collection.mutable
import scala.concurrent.Future

class MockLastTeamResultsProvider extends LastTeamResultsProvider {

  val results: mutable.Map[TeamId, EitherT[Future, Throwable, Set[TournamentResult]]] = mutable.Map()

  override def getLastTeamResults(teamId: TeamId): EitherT[Future, Throwable, Set[TournamentResult]] = results(teamId)
}
