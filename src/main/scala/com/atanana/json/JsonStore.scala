package com.atanana.json

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import com.atanana.FsHandler
import com.atanana.data.{Data, Requisition, Tournament}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.Try

class JsonStore @Inject()(fsHandler: FsHandler) {

  import JsonStore.FILE_NAME

  private implicit object RatingDateTimeFormat extends RootJsonFormat[LocalDateTime] {
    override def write(obj: LocalDateTime): JsValue = JsString(obj.format(DateTimeFormatter.ISO_DATE_TIME))

    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(value) => LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
      case _ => deserializationError("Invalid date string!")
    }
  }

  private implicit val requisitionFormat: RootJsonFormat[Requisition] = jsonFormat4(Requisition)
  private implicit val tournamentFormat: RootJsonFormat[Tournament] = jsonFormat2(Tournament)
  private implicit val dataFormat: RootJsonFormat[Data] = jsonFormat2(Data)

  def read: Data = {
    fsHandler.readFile(FILE_NAME)
      .flatMap(contents => Try {
        contents.parseJson.convertTo[Data]
      })
      .getOrElse(Data(Set.empty, Set.empty))
  }

  def write(data: Data): Unit = {
    fsHandler.writeFile(data.toJson.prettyPrint, FILE_NAME)
  }
}

object JsonStore {
  val FILE_NAME = "data.json"

  def apply(fsHandler: FsHandler): JsonStore = new JsonStore(fsHandler)
}