package com.atanana.parsers

import com.atanana.data.Team
import spray.json._

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
      id = teamJson.fields("id").asInstanceOf[JsNumber].value.toInt,
      name = teamJson.fields("teamName").asInstanceOf[JsString].value,
      city = teamJson.fields("townName").asInstanceOf[JsString].value,
      rating = teamJson.fields("teamRating").asInstanceOf[JsNumber].value.toInt,
      position = teamJson.fields("teamRatingPosition").asInstanceOf[JsNumber].value.toInt)
  }.toOption
}
