package com.atanana.ratingbot.parsers

import cats.implicits.*
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.extensions.JsonExtensions.{booleanField, intField}
import com.atanana.ratingbot.types.Ids.{TeamId, TournamentId}
import com.atanana.ratingbot.types.Pages.TournamentResultsPage
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
    ratingJson.booleanField("inRating").guard[Option].as {
      val questionsCount = teamJson.intField("questionsTotal")
      val position = teamJson.fields("position").asInstanceOf[JsNumber].value.floatValue
      val bonus = ratingJson.intField("d")
      TournamentResult(tournamentId, questionsCount, position, bonus)
    }
  }
}
