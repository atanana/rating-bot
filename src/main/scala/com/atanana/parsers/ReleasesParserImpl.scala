package com.atanana.parsers

import com.atanana.data.Release
import spray.json._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Try

class ReleasesParserImpl extends ReleasesParser {
  override def getReleases(releasesPage: String): Try[List[Release]] = Try {
    releasesPage.parseJson.asInstanceOf[JsArray]
      .elements
      .map(parseRelease)
      .toList
  }

  private def parseRelease(json: JsValue): Release = {
    val obj = json.asJsObject
    val id = obj.fields("id").asInstanceOf[JsNumber].value.toInt
    val date = LocalDateTime.parse(obj.fields("date").asInstanceOf[JsString].value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    Release(id, date)
  }
}
