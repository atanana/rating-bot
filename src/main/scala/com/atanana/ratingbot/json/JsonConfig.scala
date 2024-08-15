package com.atanana.ratingbot.json

import com.atanana.ratingbot.fs.FsHandler
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import scala.util.Try

class JsonConfig(fsHandler: FsHandler) {

  import JsonConfig.FILE_NAME

  private implicit val configFormat: RootJsonFormat[Config] = jsonFormat8(Config.apply)

  def read: Try[Config] = {
    fsHandler.readFile(FILE_NAME)
      .flatMap(contents => Try {
        contents.parseJson.convertTo[Config]
      })
  }
}

object JsonConfig {
  val FILE_NAME = "config.json"
}

case class Config(
                   tgToken: String,
                   authCookie: String,
                   chat: Long,
                   team: Int,
                   city: Int,
                   pipe: String,
                   country: Int,
                   ignoredVenues: List[String]
                 )
