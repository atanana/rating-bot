package com.atanana.json

import com.atanana.FsHandler
import spray.json.DefaultJsonProtocol._
import spray.json._

import javax.inject.Inject
import scala.util.Try

class JsonConfig @Inject()(fsHandler: FsHandler) {

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
                   chat: Int,
                   team: Int,
                   city: Int,
                   port: Int,
                   countryName: String,
                   ignoredVenues: List[String]
                 )