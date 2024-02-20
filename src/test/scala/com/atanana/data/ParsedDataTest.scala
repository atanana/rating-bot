package com.atanana.data

import com.atanana.types.Ids.TournamentId
import com.atanana.Conversions.fromIntToTournamentId
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class ParsedDataTest extends AnyWordSpecLike with Matchers {

  "ParsedData" should {

    "correct convert itself" in {
      val tournamentData = TournamentResult(1, 12, 2f, 3)
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())
      ParsedData(Set(tournamentData), Set(requisitionData)).toData shouldEqual
        Data(Set(Tournament(1, 12)), Set(requisitionData))
    }
  }
}
