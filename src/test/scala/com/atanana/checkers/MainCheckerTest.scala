package com.atanana.checkers

import java.time.LocalDateTime

import com.atanana.data._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

class MainCheckerTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  var tournamentsChecker: TournamentsChecker = _
  var requisitionsChecker: RequisitionsChecker = _
  var checker: MainChecker = _

  before {
    tournamentsChecker = stub[TournamentsChecker]
    requisitionsChecker = stub[RequisitionsChecker]
    checker = MainChecker(tournamentsChecker, requisitionsChecker)
  }

  "MainChecker" should {
    "return correct data from underlying checkers" in {
      val data = Data(Set(Tournament(1, 1)), Set(Requisition("tournament 1", "agent 1", LocalDateTime.now())))
      val newTournaments = Set(
        TournamentData(2, "tournament 2", "link 2", 2f, 2, 2)
      )
      val newRequisitions = Set(
        Requisition("tournament 4", "agent 4", LocalDateTime.now())
      )
      val tournamentsCheckResult = mock[TournamentsCheckResult]
      val requisitionsCheckResult = mock[RequisitionsCheckResult]
      (tournamentsChecker.check _).when(data.tournaments, newTournaments).returns(tournamentsCheckResult)
      (requisitionsChecker.check _).when(data.requisitions, newRequisitions).returns(requisitionsCheckResult)

      checker.check(data, newTournaments, newRequisitions) shouldEqual CheckResult(tournamentsCheckResult, requisitionsCheckResult)
    }
  }
}
