package com.atanana

import com.atanana.Connector.SITE_URL

import scalaj.http.{Http, HttpResponse}

class Connector(config: Config) {
  def get(url: String): HttpResponse[String] = {
    Http(url).asString
  }

  def getTeamPage: HttpResponse[String] = {
    Http(teamUrl).charset("cp1251").asString
  }

  def teamUrl: String = {
    SITE_URL + s"/teams.php?team_id=${config.team}&download_data=export_tournaments"
  }
}

object Connector {
  val SITE_URL = "http://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"

  def apply(config: Config): Connector = new Connector(config)
}