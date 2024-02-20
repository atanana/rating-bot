package com.atanana.checkers

import com.atanana.data.{Tournament, TournamentResult, TournamentsCheckResult}

trait TournamentsChecker {

  def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentResult]): TournamentsCheckResult
}
