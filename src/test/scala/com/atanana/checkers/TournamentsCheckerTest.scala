package com.atanana.checkers

import com.atanana.data.{ChangedTournament, Tournament, TournamentData}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TournamentsCheckerTest extends AnyWordSpecLike with Matchers {
  "TournamentsChecker" should {
    "provide valid new tournaments data" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2)
      ), Set(
        TournamentData(1, "name 1", "link 1", 1.0f, 1, 1),
        TournamentData(2, "name 2", "link 2", 2.0f, 2, 2),
        TournamentData(3, "name 3", "link 3", 3.0f, 3, 3),
        TournamentData(4, "name 4", "link 4", 4.0f, 4, 4)
      )).newTournaments shouldEqual Set(
        TournamentData(3, "name 3", "link 3", 3.0f, 3, 3),
        TournamentData(4, "name 4", "link 4", 4.0f, 4, 4)
      )
    }

    "provide no new tournaments" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2),
        Tournament(3, 3)
      ), Set(
        TournamentData(1, "name 1", "link 1", 1.0f, 1, 1),
        TournamentData(2, "name 2", "link 2", 2.0f, 2, 3),
        TournamentData(3, "name 3", "link 3", 3.0f, 3, 1)
      )).newTournaments shouldEqual Set.empty
    }

    "provide valid changed tournaments data" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2),
        Tournament(3, 3)
      ), Set(
        TournamentData(1, "name 1", "link 1", 1.0f, 1, 1),
        TournamentData(2, "name 2", "link 2", 2.0f, 2, 4),
        TournamentData(3, "name 3", "link 3", 3.0f, 3, 3)
      )).changedTournaments shouldEqual Set(
        ChangedTournament(TournamentData(2, "name 2", "link 2", 2.0f, 2, 4), 2)
      )
    }

    "provide no changed tournaments" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2)
      ), Set(
        TournamentData(1, "name 1", "link 1", 1.0f, 1, 1),
        TournamentData(2, "name 2", "link 2", 2.0f, 2, 2),
        TournamentData(3, "name 3", "link 3", 3.0f, 3, 3)
      )).changedTournaments shouldEqual Set.empty
    }

    "handle new scores with errors" in {
      TournamentsCheckerImpl().check(Set(
        Tournament(1, 1),
        Tournament(2, 2)
      ), Set(
        TournamentData(1, "name 1", "link 1", 1.0f, 1, 1),
        TournamentData(2, "name 2", "link 2", 2.0f, 2, 0),
      )).changedTournaments shouldEqual Set.empty
    }
  }
}
