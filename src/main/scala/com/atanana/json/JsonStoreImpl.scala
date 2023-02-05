package com.atanana.json

import com.atanana.data.{Data, Requisition, Tournament}
import com.atanana.fs.{FsHandler, FsHandlerImpl}
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Try

private val FILE_NAME = "data.json"

class JsonStoreImpl(fsHandler: FsHandler) extends JsonStore {

  private implicit object RatingDateTimeFormat extends RootJsonFormat[LocalDateTime] {
    override def write(obj: LocalDateTime): JsValue = JsString(obj.format(DateTimeFormatter.ISO_DATE_TIME))

    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(value) => LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
      case _ => deserializationError("Invalid date string!")
    }
  }

  private implicit val requisitionFormat: RootJsonFormat[Requisition] = jsonFormat4(Requisition.apply)
  private implicit val tournamentFormat: RootJsonFormat[Tournament] = jsonFormat2(Tournament.apply)
  private implicit val dataFormat: RootJsonFormat[Data] = jsonFormat2(Data.apply)

  override def read: Data = {
    fsHandler.readFile(FILE_NAME)
      .flatMap(contents => Try {
        contents.parseJson.convertTo[Data]
      })
      .getOrElse(Data(Set.empty, Set.empty))
  }

  override def write(data: Data): Unit = {
    fsHandler.writeFile(data.toJson.prettyPrint, FILE_NAME)
  }
}
