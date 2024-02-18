package com.atanana.parsers

import scala.util.Try
import spray.json.*
import com.atanana.extensions.JsonExtensions.intField
import com.atanana.types.Ids.TournamentId
import com.atanana.types.Pages.TeamTournamentsPage
import com.atanana.types.Pages.TeamTournamentsPage.*

class TeamTournamentsParserImpl extends TeamTournamentsParser {

  override def getTournamentIds(teamTournamentsPage: TeamTournamentsPage): Try[Set[TournamentId]] = Try {
    teamTournamentsPage.toJson.asInstanceOf[JsArray]
      .elements
      .map(json => TournamentId(json.intField("idtournament")))
      .toSet
  }
}
