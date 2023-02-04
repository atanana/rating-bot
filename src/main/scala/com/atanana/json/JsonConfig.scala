package com.atanana.json

import com.atanana.fs.FsHandler
import spray.json.DefaultJsonProtocol.*
import spray.json.*

import scala.util.Try

class JsonConfig(fsHandler: FsHandler) {

  import JsonConfig.FILE_NAME

  private implicit val configFormat: RootJsonFormat[Config] = jsonFormat9(Config)

  def read: Try[Config] = {
    fsHandler.readFile(FILE_NAME)
      .flatMap(contents => Try {
        contents.parseJson.convertTo[Config]
      })
  }
}

object JsonConfig {
  val FILE_NAME = "config.json"

  def apply(fsHandler: FsHandler): JsonConfig = new JsonConfig(fsHandler)
}

case class Config(
                   tgToken: String,
                   apiToken: String,
                   authCookie: String,
                   chat: Long,
                   team: Int,
                   city: Int,
                   port: Int,
                   country: Int,
                   ignoredVenues: List[String]
                 )