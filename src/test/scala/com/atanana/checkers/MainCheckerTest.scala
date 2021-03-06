package com.atanana.checkers

import com.atanana.data._
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class MainCheckerTest extends AnyWordSpecLike with MockFactory with Matchers {

  private val tournamentsChecker = stub[TournamentsChecker]
  private val requisitionsChecker = stub[RequisitionsChecker]
  private val checker = new MainChecker(tournamentsChecker, requisitionsChecker)

  "MainChecker" should {

    "return correct data from underlying checkers" in {
      val data = Data(Set(Tournament(1, 1)), Set(Requisition("tournament 1", "agent 1", LocalDateTime.now())))
      val newTournaments = Set(
        TournamentData(2, "tournament 2", "link 2", 2f, 2, 2)
      )
      val newRequisitions = Set(
        RequisitionData("tournament 4", 4, "agent 4", LocalDateTime.now())
      )
      val tournamentsCheckResult = TournamentsCheckResult(newTournaments, Set.empty)
      val requisitionsCheckResult = RequisitionsCheckResult(newRequisitions, Set.empty)
      (tournamentsChecker.check _).when(data.tournaments, newTournaments).returns(tournamentsCheckResult)
      (requisitionsChecker.check _).when(data.requisitions, newRequisitions).returns(requisitionsCheckResult)

      checker.check(data, ParsedData(newTournaments, newRequisitions)) shouldEqual CheckResult(tournamentsCheckResult, requisitionsCheckResult)
    }

    "return no new tournaments on first run" in {
      val data = Data(Set.empty, Set.empty)
      val tournament = TournamentData(2, "tournament 2", "link 2", 2f, 2, 2)
      val newTournaments = Set(tournament)
      (tournamentsChecker.check _).when(data.tournaments, newTournaments).returns(
        TournamentsCheckResult(Set(tournament), Set(ChangedTournament(tournament, 4)))
      )
      (requisitionsChecker.check _).when(*, *).returns(mock[RequisitionsCheckResult])

      checker.check(data, ParsedData(newTournaments, Set.empty)).tournamentsCheckResult shouldEqual TournamentsCheckResult(Set.empty, Set(ChangedTournament(tournament, 4)))
    }
  }
}
