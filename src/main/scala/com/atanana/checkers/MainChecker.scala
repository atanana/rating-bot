package com.atanana.checkers

import com.atanana.data._

class MainChecker(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker) {
  def check(storedData: Data, parsedData: ParsedData): CheckResult = {
    val tournamentsCheckResult = tournamentsChecker.check(storedData.tournaments, parsedData.tournaments)
    CheckResult(
      if (storedData.tournaments.nonEmpty) tournamentsCheckResult else tournamentsCheckResult.copy(newTournaments = Set.empty),
      requisitionsChecker.check(storedData.requisitions, parsedData.requisitions)
    )
  }
}