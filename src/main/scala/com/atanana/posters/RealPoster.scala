package com.atanana.posters

import cats.data.EitherT
import com.atanana.json.Config
import com.atanana.net.Connector
import com.typesafe.scalalogging.Logger
import sttp.client3.UriContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RealPoster(connector: Connector, config: Config) extends Poster {

  import RealPoster.logger

  private val url = uri"https://api.telegram.org/bot${config.tgToken}/sendMessage"

  private def params(message: String): Map[String, String] = Map(
    "chat_id" -> config.chat.toString,
    "text" -> message,
    "disable_web_page_preview" -> "true",
    "parse_mode" -> "Markdown"
  )

  //noinspection ConvertibleToMethodValue
  override def postAsync(message: String): EitherT[Future, Throwable, Unit] =
    connector.postAsync(url, params(message))
      .map(logger.debug(_))
}

object RealPoster {
  private val logger = Logger(classOf[RealPoster])

  def apply(connector: Connector, config: Config): RealPoster = new RealPoster(connector, config)
}