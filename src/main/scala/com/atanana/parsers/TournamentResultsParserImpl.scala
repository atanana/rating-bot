package com.atanana.parsers

import com.atanana.data.TournamentResult
import com.atanana.extensions.JsonExtensions.{booleanField, intField}
import com.atanana.types.Ids.{TeamId, TournamentId}
import com.atanana.types.Pages.TournamentResultsPage
import spray.json.{JsArray, JsNumber}

import scala.util.Try

class TournamentResultsParserImpl extends TournamentResultsParser {

  override def getTeamResults(page: TournamentResultsPage, tournamentId: TournamentId, teamId: TeamId): Try[Option[TournamentResult]] = Try {
    val teamJson = page.toJson
      .asInstanceOf[JsArray].elements.toSeq
      .map(json => json.asJsObject)
      .find(json => json.fields("team").intField("id") == teamId.value)
      .getOrElse(throw RuntimeException(s"Cannot find team $teamId on page!"))

    val ratingJson = teamJson.fields("rating").asJsObject
    if ratingJson.booleanField("inRating") then {
      val questionsCount = teamJson.intField("questionsTotal")
      val position = teamJson.fields("position").asInstanceOf[JsNumber].value.floatValue
      val bonus = ratingJson.intField("d")
      Some(TournamentResult(tournamentId, questionsCount, position, bonus))
    } else None
  }
}
