package com.atanana

import com.atanana.types.Ids.TournamentId

object Conversions {

  implicit def fromIntToTournamentId: Conversion[Int, TournamentId] = TournamentId(_)
}
