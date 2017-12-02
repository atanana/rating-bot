package com.atanana.checkers

import javax.inject.Inject

import com.atanana.data._

class MainChecker @Inject()(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker) {
  def check(storedData: Data, parsedData: ParsedData): CheckResult = {
    val tournamentsCheckResult = tournamentsChecker.check(storedData.tournaments, parsedData.tournaments)
    CheckResult(
      if (storedData.tournaments.nonEmpty) tournamentsCheckResult else tournamentsCheckResult.copy(newTournaments = Set.empty),
      parsedData.requisitions.fold(
        _ => RequisitionsCheckResult.EMPTY,
        requisitions => requisitionsChecker.check(storedData.requisitions, requisitions)
      )
    )
  }
}