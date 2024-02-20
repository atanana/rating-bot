package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.types.Ids.TournamentId
import com.atanana.ratingbot.types.Pages.TeamTournamentsPage

import scala.util.Try

trait TeamTournamentsParser {

  def getTournamentIds(teamTournamentsPage: TeamTournamentsPage): Try[Set[TournamentId]]
}
