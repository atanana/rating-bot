package com.atanana.parsers

import scala.util.Try
import spray.json.*
import com.atanana.extensions.JsonExtensions.intField

class TeamTournamentsParserImpl extends TeamTournamentsParser {

  override def getTournamentIds(teamTournamentsPage: String): Try[Set[Int]] = Try {
    teamTournamentsPage.parseJson.asInstanceOf[JsArray]
      .elements
      .map(_.intField("idtournament"))
      .toSet
  }
}
