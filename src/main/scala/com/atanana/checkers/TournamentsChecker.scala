package com.atanana.checkers

import com.atanana.data.{Tournament, TournamentData, TournamentsCheckResult}

trait TournamentsChecker {

  def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentData]): TournamentsCheckResult
}
