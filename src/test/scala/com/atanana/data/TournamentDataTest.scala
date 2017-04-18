package com.atanana.data

import org.scalatest.{Matchers, WordSpecLike}

class TournamentDataTest extends WordSpecLike with Matchers {
  "TournamentData" should {
    "correct convert itself" in {
      TournamentData(1, "tournament 1", "link 1", 2f, 3, 12).toTournament shouldEqual Tournament(1, 12)
    }
  }
}
