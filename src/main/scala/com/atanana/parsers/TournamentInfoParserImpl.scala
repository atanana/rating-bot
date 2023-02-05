package com.atanana.parsers

import spray.json.*

import scala.util.Try

class TournamentInfoParserImpl extends TournamentInfoParser {
  override def getQuestionsCount(tournamentInfo: String): Try[Int] = Try {
    val tournamentJson = tournamentInfo.parseJson.asInstanceOf[JsObject]
    val questionsCountJson = tournamentJson.fields("questionQty").asInstanceOf[JsObject]
    questionsCountJson.fields.values.map(_.asInstanceOf[JsNumber].value).sum.toInt
  }
}