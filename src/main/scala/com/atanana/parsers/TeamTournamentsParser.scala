package com.atanana.parsers

import com.atanana.types.Pages.TeamTournamentsPage

import scala.util.Try

trait TeamTournamentsParser {

  def getTournamentIds(teamTournamentsPage: TeamTournamentsPage): Try[Set[Int]]
}
