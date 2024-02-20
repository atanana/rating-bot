package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.types.Ids.{TeamId, TournamentId}
import com.atanana.ratingbot.types.Pages.TournamentResultsPage

import scala.util.Try

trait TournamentResultsParser {

  def getTeamResults(page: TournamentResultsPage, tournamentId: TournamentId, teamId: TeamId): Try[Option[TournamentResult]]
}
