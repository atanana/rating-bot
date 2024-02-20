package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.parsers.TeamTournamentsParser
import com.atanana.ratingbot.types.Ids.TournamentId
import com.atanana.ratingbot.types.Pages.TeamTournamentsPage

import scala.collection.mutable
import scala.util.Try

class MockTeamTournamentsParser extends TeamTournamentsParser {

  var results: mutable.Map[TeamTournamentsPage, Try[Set[TournamentId]]] = mutable.Map()

  override def getTournamentIds(teamTournamentsPage: TeamTournamentsPage): Try[Set[TournamentId]] = results(teamTournamentsPage)
}
