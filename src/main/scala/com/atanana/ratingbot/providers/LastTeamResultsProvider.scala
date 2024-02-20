package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.types.Ids.TeamId

import scala.concurrent.Future

trait LastTeamResultsProvider {

  def getLastTeamResults(teamId: TeamId): EitherT[Future, Throwable, Set[TournamentResult]]
}
