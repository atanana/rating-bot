package com.atanana.posters

import com.atanana.Connector
import com.atanana.json.Config
import com.typesafe.scalalogging.Logger
import javax.inject.Inject

class RealPoster @Inject()(connector: Connector, config: Config) extends Poster {

  import RealPoster.logger

  override def post(message: String): Unit = {
    val url = s"https://api.telegram.org/bot${config.token}/sendMessage"
    val params = Map(
      "chat_id" -> config.chat.toString,
      "text" -> message
    )
    val response = connector.post(url, params)
    logger.debug(response)
  }
}

object RealPoster {
  val logger = Logger(classOf[RealPoster])

  def apply(connector: Connector, config: Config): RealPoster = new RealPoster(connector, config)
}