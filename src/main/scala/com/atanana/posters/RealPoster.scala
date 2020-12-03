package com.atanana.posters

import com.atanana.Connector
import com.atanana.json.Config
import com.typesafe.scalalogging.Logger
import sttp.client3.UriContext

import javax.inject.Inject

class RealPoster @Inject()(connector: Connector, config: Config) extends Poster {

  import RealPoster.logger

  private val url = uri"https://api.telegram.org/bot${config.token}/sendMessage"

  override def post(message: String): Either[String, Unit] =
    connector.post(url, params(message))
      .map(response => logger.debug(response))

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