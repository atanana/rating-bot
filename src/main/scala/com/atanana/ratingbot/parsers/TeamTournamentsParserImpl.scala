package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.extensions.JsonExtensions.intField
import com.atanana.ratingbot.types.Ids.TournamentId
import com.atanana.ratingbot.types.Pages.TeamTournamentsPage
import com.atanana.ratingbot.types.Pages.TeamTournamentsPage.*
import spray.json.*

import scala.util.Try

class TeamTournamentsParserImpl extends TeamTournamentsParser {

  override def getTournamentIds(teamTournamentsPage: TeamTournamentsPage): Try[Set[TournamentId]] = Try {
    teamTournamentsPage.toJson.asInstanceOf[JsArray]
      .elements
      .map(json => TournamentId(json.intField("idtournament")))
      .toSet
  }
}
