package com.atanana.checkers

import javax.inject.Inject

import com.atanana.data._

class MainChecker @Inject()(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker) {
  def check(storedData: Data, newTournaments: Set[TournamentData], newRequisitions: Set[RequisitionData]): CheckResult = {
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