package com.atanana.ratingbot.posters

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.net.{Connector, ConnectorImpl}
import com.typesafe.scalalogging.Logger
import sttp.client3.UriContext

class RealPoster(connector: Connector, config: Config) extends Poster {

  import RealPoster.logger

  private val url = uri"https://api.telegram.org/bot${config.tgToken}/sendMessage"

  private def params(message: String): Map[String, String] = Map(
    "chat_id" -> config.chat.toString,
    "text" -> message,
    "disable_web_page_preview" -> "true",
    "parse_mode" -> "Markdown"
  )

  override def postAsync(message: String): EitherT[IO, Throwable, Unit] =
    connector.postAsync(url, params(message))
      .map(logger.debug)
}

object RealPoster {
  private val logger = Logger(classOf[RealPoster])
}