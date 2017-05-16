package com.atanana

import javax.inject.Inject

import com.atanana.Connector.SITE_URL

import scalaj.http.{Http, HttpResponse}

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {
  def get(url: String): HttpResponse[String] = {
    netWrapper.get(url)
  }

  def getTeamPage: String = {
    getPage(teamUrl)
  }

  private def teamUrl: String = {
    SITE_URL + s"/teams.php?team_id=${config.team}&download_data=export_tournaments"
  }

  def getTournamentPage(id: Int): String = {
    getPage(tournamentUrl(id))
  }

  private def tournamentUrl(id: Int): String = {
    SITE_URL + "/tournament/" + id
  }

  def getRequisitionPage: String = {
    getPage(requisitionUrl)
  }

  private def requisitionUrl: String = {
    SITE_URL + "/synch_town/" + config.city
  }

  def getTeamsPage: String = {
    getPage(SITE_URL + "/teams.php")
  }

  private def getPage(url: String) = netWrapper.getPage(url)
}

object Connector {
  val SITE_URL = "http://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class NetWrapper {
  def getPage(url: String): String = Http(url).charset("cp1251").asString.body

  def get(url: String): HttpResponse[String] = Http(url).asString
}