package com.atanana

import javax.inject.Inject

import scala.util.Try

class Configurator @Inject()(systemWrapper: SystemWrapper) {
  def config: Try[Config] = {
    Try {
      val token = getStringRequiredValue("token")
      val chat = getIntRequiredValue("chat")
      val team = getIntRequiredValue("team")
      val city = getIntRequiredValue("city")
      val cityName = getStringRequiredValue("cityName")
      val countryName = getStringRequiredValue("countryName")
      val port = getIntOptionalValue("port").getOrElse(11000)
      Config(token, chat, team, city, port, cityName, countryName)
    }
  }

  private def getIntRequiredValue(key: String): Int = {
    getStringRequiredValue(key).toInt
  }

  private def getStringRequiredValue(key: String): String = {
    getStringOptionalValue(key).getOrElse(throw new RuntimeException(s"$key is not set!"))
  }

  private def getIntOptionalValue(key: String): Option[Int] = {
    getStringOptionalValue(key).map(_.toInt)
  }

  private def getStringOptionalValue(key: String): Option[String] = {
    val result = systemWrapper.get(key)
    if (result.isEmpty) None else Some(result)
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

case class Config(token: String,
                  chat: Int,
                  team: Int,
                  city: Int,
                  port: Int,
                  cityName: String,
                  countryName: String)