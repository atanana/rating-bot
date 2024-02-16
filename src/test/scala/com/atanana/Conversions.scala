package com.atanana

import com.atanana.types.Ids.{ReleaseId, TournamentId}

object Conversions {

  implicit def fromIntToTournamentId: Conversion[Int, TournamentId] = TournamentId(_)

  implicit def fromIntToReleaseId: Conversion[Int, ReleaseId] = ReleaseId(_)
}
