package com.atanana.ratingbot.checkers

import com.atanana.ratingbot.data.{Tournament, TournamentResult, TournamentsCheckResult}

trait TournamentsChecker {

  def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentResult]): TournamentsCheckResult
}
