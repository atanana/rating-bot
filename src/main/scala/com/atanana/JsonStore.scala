package com.atanana

import com.atanana.data.{Data, Tournament}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.util.Try

class JsonStore(private val fsHandler: FsHandler) {

  import JsonStore.FILE_NAME

  private implicit val tournamentFormat = jsonFormat2(Tournament)
  private implicit val dataFormat = jsonFormat1(Data)

  def read: Data = {
    fsHandler.readFile(FILE_NAME)
      .flatMap(contents => Try(contents.parseJson.convertTo[Data]))
      .getOrElse(Data(Set.empty))
  }

  def write(data: Data): Unit = {
    fsHandler.writeFile(data.toJson.prettyPrint, FILE_NAME)
  }
}

object JsonStore {
  val FILE_NAME = "data.json"

  def apply(fsHandler: FsHandler): JsonStore = new JsonStore(fsHandler)
}