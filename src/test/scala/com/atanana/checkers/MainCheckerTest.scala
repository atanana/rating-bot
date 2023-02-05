package com.atanana.checkers

import com.atanana.data.*
import com.atanana.mocks.{MockRequisitionsChecker, MockTournamentsChecker}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class MainCheckerTest extends AnyWordSpecLike with Matchers {

  private val tournamentsChecker = new MockTournamentsChecker()
  private val requisitionsChecker = new MockRequisitionsChecker()
  private val checker = new MainCheckerImpl(tournamentsChecker, requisitionsChecker)

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
      tournamentsChecker.checkResults.put((data.tournaments, newTournaments), tournamentsCheckResult)
      val requisitionsCheckResult = RequisitionsCheckResult(newRequisitions, Set.empty)
      requisitionsChecker.checkResults.put((data.requisitions, newRequisitions), requisitionsCheckResult)

      checker.check(data, ParsedData(newTournaments, newRequisitions)) shouldEqual CheckResult(tournamentsCheckResult, requisitionsCheckResult)
    }

    "return no new tournaments on first run" in {
      val data = Data(Set.empty, Set.empty)
      val tournament = TournamentData(2, "tournament 2", "link 2", 2f, 2, 2)
      val newTournaments = Set(tournament)
      tournamentsChecker.checkResults.put((data.tournaments, newTournaments), TournamentsCheckResult(Set(tournament), Set(ChangedTournament(tournament, 4))))
      requisitionsChecker.checkResults.put((Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))

      checker.check(data, ParsedData(newTournaments, Set.empty)).tournamentsCheckResult shouldEqual TournamentsCheckResult(Set.empty, Set(ChangedTournament(tournament, 4)))
    }
  }
}
