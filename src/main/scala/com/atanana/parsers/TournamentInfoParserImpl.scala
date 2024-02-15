package com.atanana.parsers

import spray.json.*

import scala.util.Try

class TournamentInfoParserImpl extends TournamentInfoParser {
  override def getQuestionsCount(tournamentInfo: String): Try[Int] = Try {
    val tournamentJson = tournamentInfo.parseJson.asJsObject
    val questionsCountJson = tournamentJson.fields("questionQty").asJsObject
    questionsCountJson.fields.values.map(_.asInstanceOf[JsNumber].value).sum.toInt
  }
}
