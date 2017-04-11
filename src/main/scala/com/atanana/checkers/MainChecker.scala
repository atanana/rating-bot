package com.atanana.checkers

import com.atanana.data.{CheckResult, Data, Requisition, TournamentData}

class MainChecker(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker) {
  def check(storedData: Data, newTournaments: Set[TournamentData], newRequisitions: Set[Requisition]): CheckResult = {
    CheckResult(
      tournamentsChecker.check(storedData.tournaments, newTournaments),
      requisitionsChecker.check(storedData.requisitions, newRequisitions)
    )
  }
}

object MainChecker {
  def apply(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker): MainChecker = new MainChecker(tournamentsChecker, requisitionsChecker)
}