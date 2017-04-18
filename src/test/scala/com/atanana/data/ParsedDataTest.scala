package com.atanana.data

import java.time.LocalDateTime

import org.scalatest.{Matchers, WordSpecLike}

class ParsedDataTest extends WordSpecLike with Matchers {
  "ParsedData" should {
    "correct convert itself" in {
      val tournamentData = TournamentData(1, "tournament 1", "link 1", 2f, 3, 12)
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())
      ParsedData(Set(tournamentData), Set(requisitionData)).toData shouldEqual
        Data(Set(tournamentData.toTournament), Set(requisitionData.toRequisition))
    }
  }
}
