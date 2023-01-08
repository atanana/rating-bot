package com.atanana.parsers

import com.atanana.data.Release
import spray.json._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Try

class ReleasesParser {
  def getReleases(releasesPage: String): Try[List[Release]] = Try {
    val releasesJson = releasesPage.parseJson.asInstanceOf[JsObject]
    releasesJson.fields("hydra:member").asInstanceOf[JsArray]
      .elements
      .map(parseRelease)
      .toList
  }

  private def parseRelease(json: JsValue): Release = {
    val obj = json.asJsObject
    val id = obj.fields("id").toString().toInt
    val date = LocalDateTime.parse(obj.fields("date").asInstanceOf[JsString].value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    Release(id, date)
  }
}
