package com.atanana.checkers

import com.atanana.data.{CheckResult, Data, Requisition, TournamentData}

class MainChecker(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker) {
  def check(storedData: Data, newTournaments: Set[TournamentData], newRequisitions: Set[Requisition]): CheckResult = {
    val tournamentsCheckResult = tournamentsChecker.check(storedData.tournaments, newTournaments)
    CheckResult(
      if (storedData.tournaments.nonEmpty) tournamentsCheckResult else tournamentsCheckResult.copy(newTournaments = Set.empty),
      requisitionsChecker.check(storedData.requisitions, newRequisitions)
    )
  }
}

object MainChecker {
  def apply(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker): MainChecker = new MainChecker(tournamentsChecker, requisitionsChecker)
}