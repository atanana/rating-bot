package com.atanana.parsers

import spray.json._

import scala.util.Try

class TournamentInfoParser {
  def getQuestionsCount(tournamentInfo: String): Try[Int] = Try {
    val tournamentJson = tournamentInfo.parseJson.asInstanceOf[JsArray].elements.head.asJsObject
    tournamentJson.fields("questions_total").asInstanceOf[JsString].value.toInt
  }
}
