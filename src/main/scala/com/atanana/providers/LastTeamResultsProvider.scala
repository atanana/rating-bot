package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.TournamentResult
import com.atanana.types.Ids.TeamId

import scala.concurrent.Future

trait LastTeamResultsProvider {

  def getLastTeamResults(teamId: TeamId): EitherT[Future, Throwable, Set[TournamentResult]]
}
