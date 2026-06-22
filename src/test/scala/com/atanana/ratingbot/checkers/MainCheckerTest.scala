package com.atanana.ratingbot.checkers

import com.atanana.ratingbot
import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.data.*
import com.atanana.ratingbot.mocks.MockTournamentsChecker
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class MainCheckerTest extends AnyWordSpecLike with Matchers {

  private val tournamentsChecker = new MockTournamentsChecker()
  private val checker = new MainCheckerImpl(tournamentsChecker)

  "MainChecker" should {

    "return correct data from underlying checkers" in {
      val data = Data(Set(Tournament(1, 1)))
      val newTournaments = Set(
        TournamentResult(2, 2, 2f, 2)
      )
      val tournamentsCheckResult = TournamentsCheckResult(newTournaments, Set.empty)
      tournamentsChecker.checkResults.put((data.tournaments, newTournaments), tournamentsCheckResult)

      checker.check(data, ParsedData(newTournaments)) shouldEqual CheckResult(tournamentsCheckResult)
    }

    "return no new tournaments on first run" in {
      val data = Data(Set.empty)
      val tournament = TournamentResult(2, 2, 2f, 2)
      val newTournaments = Set(tournament)
      tournamentsChecker.checkResults.put((data.tournaments, newTournaments), TournamentsCheckResult(Set(tournament), Set(ChangedTournament(tournament, 4))))

      checker.check(data, ratingbot.data.ParsedData(newTournaments)).tournamentsCheckResult shouldEqual TournamentsCheckResult(Set.empty, Set(ChangedTournament(tournament, 4)))
    }
  }
}
