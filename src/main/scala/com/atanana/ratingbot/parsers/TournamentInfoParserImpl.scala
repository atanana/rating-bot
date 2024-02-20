package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.TournamentInfo
import com.atanana.ratingbot.extensions.JsonExtensions.stringField
import com.atanana.ratingbot.types.Pages.TournamentInfoPage
import com.atanana.ratingbot.types.Pages.TournamentInfoPage.*
import spray.json.*

import scala.util.Try

class TournamentInfoParserImpl extends TournamentInfoParser {

  override def getTournamentInfo(page: TournamentInfoPage): Try[TournamentInfo] = Try {
    val tournamentJson = page.toJson.asJsObject
    val name = tournamentJson.stringField("name")
    val questionsCountJson = tournamentJson.fields("questionQty").asJsObject
    val questionsCount = questionsCountJson.fields.values.map(_.asInstanceOf[JsNumber].value).sum.toInt
    TournamentInfo(name, questionsCount)
  }
}
