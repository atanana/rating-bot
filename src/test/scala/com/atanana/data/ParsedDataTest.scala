package com.atanana.data

import com.atanana.types.Ids.TournamentId
import com.atanana.Conversions.fromIntToTournamentId
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class ParsedDataTest extends AnyWordSpecLike with Matchers {

  "ParsedData" should {

    "correct convert itself" in {
      val tournamentData = TournamentData(1, "tournament 1", "link 1", 2f, 3, 12)
      val requisitionData = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())
      ParsedData(Set(tournamentData), Set(requisitionData)).toData shouldEqual
        Data(Set(tournamentData), Set(requisitionData))
    }
  }
}
