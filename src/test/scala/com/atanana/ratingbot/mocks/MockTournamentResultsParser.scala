package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.parsers.TournamentResultsParser
import com.atanana.ratingbot.types.Ids.{TeamId, TournamentId}
import com.atanana.ratingbot.types.Pages.TournamentResultsPage

import scala.collection.mutable
import scala.util.Try

class MockTournamentResultsParser extends TournamentResultsParser {

  var results: mutable.Map[(TournamentResultsPage, TournamentId, TeamId), Try[Option[TournamentResult]]] = mutable.Map()

  override def getTeamResults(page: TournamentResultsPage, tournamentId: TournamentId, teamId: TeamId): Try[Option[TournamentResult]] = results((page, tournamentId, teamId))
}
