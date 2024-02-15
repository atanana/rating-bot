package com.atanana.parsers

import scala.util.Try
import spray.json.*

class TeamTournamentsParserImpl extends TeamTournamentsParser {

  override def getTournamentIds(teamTournamentsPage: String): Try[Set[Int]] = Try {
    teamTournamentsPage.parseJson.asInstanceOf[JsArray]
      .elements
      //todo refactor
      .map(json => json.asJsObject.fields("idtournament").asInstanceOf[JsNumber].value.toInt)
      .toSet
  }
}
