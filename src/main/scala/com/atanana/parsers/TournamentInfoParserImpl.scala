package com.atanana.parsers

import com.atanana.data.TournamentInfo
import com.atanana.types.Pages.TournamentInfoPage
import com.atanana.types.Pages.TournamentInfoPage.*
import com.atanana.extensions.JsonExtensions.stringField
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
