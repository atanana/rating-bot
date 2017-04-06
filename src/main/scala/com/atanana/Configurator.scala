package com.atanana

import scala.util.Try

class Configurator(private val systemWrapper: SystemWrapper) {
  def config: Try[Config] = {
    Try({
      val token = getStringValue("token")
      val chat = getIntValue("chat")
      val team = getIntValue("team")
      Config(token, chat, team)
    })
  }

  private def getIntValue(key: String): Int = {
    getStringValue(key).toInt
  }

  private def getStringValue(key: String): String = {
    val result = systemWrapper.get(key)
    if (result.isEmpty) {
      throw new RuntimeException(s"$key is not set!")
    }
    result
  }
}

object Configurator {
  def apply(systemWrapper: SystemWrapper): Configurator = new Configurator(systemWrapper)
}

class SystemWrapper {
  def get(key: String): String = {
    System.getProperty(key, "")
  }
}

case class Config(token: String, chat: Int, team: Int)