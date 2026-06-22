package com.atanana.ratingbot.data

import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.data.{Data, ParsedData, Tournament, TournamentResult}
import com.atanana.ratingbot.types.Ids.TournamentId
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class ParsedDataTest extends AnyWordSpecLike with Matchers {

  "ParsedData" should {

    "correct convert itself" in {
      val tournamentData = TournamentResult(1, 12, 2f, 3)
      ParsedData(Set(tournamentData)).toData shouldEqual
        Data(Set(Tournament(1, 12)))
    }
  }
}
