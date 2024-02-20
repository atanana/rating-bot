package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.Team
import com.atanana.ratingbot.extensions.JsonExtensions.*
import spray.json.*

import scala.util.Try

class TeamsPageParserImpl extends TeamsPageParser {
  override def getTeams(teamsContent: String): List[Team] = {
    val json = teamsContent.parseJson.asJsObject
    json.fields("data").asInstanceOf[JsArray].elements
      .flatMap(parseTeam(_).toList).toList
  }

  private def parseTeam(json: JsValue) = Try {
    val teamJson = json.asJsObject
    Team(
      id = teamJson.intField("id"),
      name = teamJson.stringField("teamName"),
      city = teamJson.stringField("townName"),
      rating = teamJson.intField("teamRating"),
      position = teamJson.intField("teamRatingPosition"))
  }.toOption
}
