package com.atanana.data

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import com.atanana.Conversions.fromIntToTournamentId

class TournamentDataTest extends AnyWordSpecLike with Matchers {
  "TournamentData" should {
    "correct convert itself" in {
      TournamentData(1, "tournament 1", "link 1", 2f, 3, 12).toTournament shouldEqual Tournament(1, 12)
    }
  }
}
