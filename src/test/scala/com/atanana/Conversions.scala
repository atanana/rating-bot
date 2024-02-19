package com.atanana

import com.atanana.types.Ids.{ReleaseId, TeamId, TournamentId}
import com.atanana.types.Pages.{TeamTournamentsPage, TournamentResultsPage}

object Conversions {

  implicit def fromIntToTournamentId: Conversion[Int, TournamentId] = TournamentId(_)

  implicit def fromIntToReleaseId: Conversion[Int, ReleaseId] = ReleaseId(_)

  implicit def fromIntToTeamId: Conversion[Int, TeamId] = TeamId(_)

  implicit def fromStringToTeamTournamentsPage: Conversion[String, TeamTournamentsPage] = TeamTournamentsPage(_)

  implicit def fromStringToTournamentResultsPage: Conversion[String, TournamentResultsPage] = TournamentResultsPage(_)
}
