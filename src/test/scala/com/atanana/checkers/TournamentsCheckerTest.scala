package com.atanana.checkers

import com.atanana.data.{ChangedTournament, Tournament, TournamentResult}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import com.atanana.Conversions.fromIntToTournamentId

class TournamentsCheckerTest extends AnyWordSpecLike with Matchers {

  "TournamentsChecker" should {

    "provide valid new tournaments data" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2)
      ), Set(
        TournamentResult(1, 1, 1.0f, 1),
        TournamentResult(2, 2, 2.0f, 2),
        TournamentResult(3, 3, 3.0f, 3),
        TournamentResult(4, 4, 4.0f, 4)
      )).newTournaments shouldEqual Set(
        TournamentResult(3, 3, 3.0f, 3),
        TournamentResult(4, 4, 4.0f, 4)
      )
    }

    "provide no new tournaments" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2),
        Tournament(3, 3)
      ), Set(
        TournamentResult(1, 1, 1.0f, 1),
        TournamentResult(2, 3, 2.0f, 2),
        TournamentResult(3, 1, 3.0f, 3)
      )).newTournaments shouldEqual Set.empty
    }

    "provide valid changed tournaments data" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2),
        Tournament(3, 3)
      ), Set(
        TournamentResult(1, 1, 1.0f, 1),
        TournamentResult(2, 4, 2.0f, 2),
        TournamentResult(3, 3, 3.0f, 3)
      )).changedTournaments shouldEqual Set(
        ChangedTournament(TournamentResult(2, 4, 2.0f, 2), 2)
      )
    }

    "provide no changed tournaments" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2)
      ), Set(
        TournamentResult(1, 1, 1.0f, 1),
        TournamentResult(2, 2, 2.0f, 2),
        TournamentResult(3, 3, 3.0f, 3)
      )).changedTournaments shouldEqual Set.empty
    }
  }
}
