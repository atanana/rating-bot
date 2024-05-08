package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.types.Ids.TeamId

trait LastTeamResultsProvider {

  def getLastTeamResults(teamId: TeamId): EitherT[IO, Throwable, Set[TournamentResult]]
}
