package com.atanana.parsers

import com.atanana.data.Team
import spray.json._

class TeamsPageParser {
  def getTeams(teamsContent: String): List[Team] = {
    val json = teamsContent.parseJson.asJsObject
    json.fields("data").asInstanceOf[JsArray].elements
      .map(parseTeam).toList
  }

  private def parseTeam(json: JsValue): Team = {
    val teamJson = json.asJsObject
    Team(
      id = teamJson.fields("id").toString().toInt,
      name = teamJson.fields("teamName").asInstanceOf[JsString].value,
      city = teamJson.fields("townName").asInstanceOf[JsString].value,
      rating = teamJson.fields("teamRating").toString().toInt,
      position = teamJson.fields("teamRatingPosition").toString().toInt)
  }
}
