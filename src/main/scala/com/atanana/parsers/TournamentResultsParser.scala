package com.atanana.parsers

import com.atanana.data.TournamentResult
import com.atanana.types.Ids.{TeamId, TournamentId}
import com.atanana.types.Pages.TournamentResultsPage

import scala.util.Try

trait TournamentResultsParser {

  def getTeamResults(page: TournamentResultsPage, tournamentId: TournamentId, teamId: TeamId): Try[Option[TournamentResult]]
}
