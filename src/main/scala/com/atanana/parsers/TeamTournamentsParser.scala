package com.atanana.parsers

import scala.util.Try

trait TeamTournamentsParser {

  def getTournamentIds(teamTournamentsPage: String): Try[Set[Int]]
}
