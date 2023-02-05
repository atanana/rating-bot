package com.atanana.mocks

import com.atanana.checkers.TournamentsChecker
import com.atanana.data.{Tournament, TournamentData, TournamentsCheckResult}

import scala.collection.mutable

class MockTournamentsChecker extends TournamentsChecker {

  val checkResults: mutable.Map[(Set[Tournament], Set[Tournament]), TournamentsCheckResult] = mutable.Map[(Predef.Set[Tournament], Predef.Set[Tournament]), TournamentsCheckResult]()

  override def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentData]): TournamentsCheckResult =
    checkResults((oldTournaments, newTournaments))
}
