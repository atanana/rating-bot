package com.atanana

import java.net.URLEncoder.encode

import com.typesafe.scalalogging.Logger

import scalaj.http.HttpResponse

class Poster(private val connector: Connector, private val config: Config) {

  import Poster.logger

  def post(message: String): Unit = {
    val encodedMessage: String = encode(message, "UTF-8")
    val response: HttpResponse[String] = connector.get(url(encodedMessage))
    logger.debug(response.body)
  }

  def url(message: String): String = {
    s"https://api.vk.com/method/messages.send?chat_id=${config.chat}&message=$message&access_token=${config.token}&v=5.57"
  }
}

object Poster {
  val logger = Logger(classOf[Poster])

  def apply(connector: Connector, config: Config): Poster = new Poster(connector, config)
}