package com.atanana.posters

import com.atanana.Connector
import com.atanana.json.Config
import com.typesafe.scalalogging.Logger
import javax.inject.Inject

class RealPoster @Inject()(connector: Connector, config: Config) extends Poster {

  import RealPoster.logger

  private val url = s"https://api.telegram.org/bot${config.token}/sendMessage"

  override def post(message: String): Unit = {
    val response = connector.post(url, params(message))
    logger.debug(response)
  }

  private def params(message: String): Map[String, String] = Map(
    "chat_id" -> config.chat.toString,
    "text" -> message,
    "disable_web_page_preview" -> "true",
    "parse_mode" -> "Markdown"
  )
}

object RealPoster {
  private val logger = Logger(classOf[RealPoster])

  def apply(connector: Connector, config: Config): RealPoster = new RealPoster(connector, config)
}