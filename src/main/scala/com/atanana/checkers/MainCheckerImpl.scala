package com.atanana.checkers

import com.atanana.data.*
import com.typesafe.scalalogging.Logger

class MainCheckerImpl(tournamentsChecker: TournamentsChecker, requisitionsChecker: RequisitionsChecker) extends MainChecker {

  private val logger = Logger("PollingDataProvider")

  override def check(storedData: Data, parsedData: ParsedData): CheckResult = {
    val tournamentsCheckResult = tournamentsChecker.check(storedData.tournaments, parsedData.tournaments)
    val result = CheckResult(
      if storedData.tournaments.nonEmpty then tournamentsCheckResult else tournamentsCheckResult.copy(newTournaments = Set.empty),
      requisitionsChecker.check(storedData.requisitions, parsedData.requisitions)
    )
    logger.debug(s"Check result $result")
    result
  }
}