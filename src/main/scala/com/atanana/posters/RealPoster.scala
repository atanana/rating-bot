package com.atanana.posters

import java.net.URLEncoder.encode
import javax.inject.Inject

import com.atanana.Connector
import com.atanana.json.Config
import com.typesafe.scalalogging.Logger

import scalaj.http.HttpResponse

class RealPoster @Inject()(connector: Connector, config: Config) extends Poster {

  import RealPoster.logger

  override def post(message: String): Unit = {
    val encodedMessage: String = encode(message, "UTF-8")
    val response: HttpResponse[String] = connector.get(url(encodedMessage))
    logger.debug(response.body)
  }

  private def url(message: String): String = {
    s"https://api.vk.com/method/messages.send?chat_id=${config.chat}&message=$message&access_token=${config.token}&v=5.57"
  }
}

object RealPoster {
  val logger = Logger(classOf[RealPoster])

  def apply(connector: Connector, config: Config): RealPoster = new RealPoster(connector, config)
}