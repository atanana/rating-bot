package com.atanana

import com.atanana.types.Ids.{ReleaseId, TeamId, TournamentId}

object Conversions {

  implicit def fromIntToTournamentId: Conversion[Int, TournamentId] = TournamentId(_)

  implicit def fromIntToReleaseId: Conversion[Int, ReleaseId] = ReleaseId(_)

  implicit def fromIntToTeamId: Conversion[Int, TeamId] = TeamId(_)
}
