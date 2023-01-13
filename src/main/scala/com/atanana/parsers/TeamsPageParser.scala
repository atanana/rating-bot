package com.atanana.parsers

import com.atanana.data.Team
import spray.json._

import scala.util.Try

class TeamsPageParser {
  def getTeams(teamsContent: String): List[Team] = {
    val json = teamsContent.parseJson.asJsObject
    json.fields("data").asInstanceOf[JsArray].elements
      .flatMap(parseTeam(_).toList).toList
  }

  private def parseTeam(json: JsValue) = Try {
    val teamJson = json.asJsObject
    Team(
      id = teamJson.fields("id").toString().toInt,
      name = teamJson.fields("teamName").asInstanceOf[JsString].value,
      city = teamJson.fields("townName").asInstanceOf[JsString].value,
      rating = teamJson.fields("teamRating").toString().toInt,
      position = teamJson.fields("teamRatingPosition").toString().toInt)
  }.toOption
}
