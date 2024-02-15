package com.atanana.parsers

import com.atanana.data.Release
import com.atanana.extensions.JsonExtensions.*
import spray.json.*

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
    val id = obj.intField("id")
    val date = LocalDateTime.parse(obj.stringField("date"), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    Release(id, date)
  }
}
