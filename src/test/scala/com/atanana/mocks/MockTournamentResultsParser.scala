package com.atanana.mocks

import com.atanana.data.TournamentResult
import com.atanana.parsers.TournamentResultsParser
import com.atanana.types.Ids.{TeamId, TournamentId}
import com.atanana.types.Pages.TournamentResultsPage

import scala.collection.mutable
import scala.util.Try

class MockTournamentResultsParser extends TournamentResultsParser {

  var results: mutable.Map[(TournamentResultsPage, TournamentId, TeamId), Try[Option[TournamentResult]]] = mutable.Map()

  override def getTeamResults(page: TournamentResultsPage, tournamentId: TournamentId, teamId: TeamId): Try[Option[TournamentResult]] = results((page, tournamentId, teamId))
}
