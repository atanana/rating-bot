package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.checkers.TournamentsChecker
import com.atanana.ratingbot.data.{Tournament, TournamentResult, TournamentsCheckResult}

import scala.collection.mutable

class MockTournamentsChecker extends TournamentsChecker {

  val checkResults: mutable.Map[(Set[Tournament], Set[TournamentResult]), TournamentsCheckResult] = mutable.Map()

  override def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentResult]): TournamentsCheckResult =
    checkResults((oldTournaments, newTournaments))
}
