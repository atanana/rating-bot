package com.atanana

import java.net.URLEncoder.encode

import com.atanana.Connector.SITE_URL
import com.atanana.json.Config
import javax.inject.Inject
import scalaj.http.{Http, HttpResponse}

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {
  def get(url: String): HttpResponse[String] = netWrapper.get(url)

  def getTeamPage: String = getPage(teamUrl)

  private def teamUrl: String = {
    SITE_URL + s"/teams.php?team_id=${config.team}&download_data=export_tournaments"
  }

  private def getPage(url: String) = netWrapper.getPage(url)

  def getTournamentPage(id: Int): String = getPage(tournamentUrl(id))

  private def tournamentUrl(id: Int): String = SITE_URL + "/tournament/" + id

  def getRequisitionPage: String = getPage(requisitionUrl)

  private def requisitionUrl: String = SITE_URL + "/synch_town/" + config.city

  def getTeamsPage: String = getPage(SITE_URL + "/teams.php?dont_show_irregulars=on")

  def getCityTeamsPage: String = getPage(SITE_URL + s"/teams.php?town=${encode(config.cityName, "cp1251")}&dont_show_irregulars=on")

  def getCountryTeamsPage: String = getPage(SITE_URL + s"/teams.php?country=${encode(config.countryName, "cp1251")}&dont_show_irregulars=on")

  def getTournamentRequisitionsPage(tournamentId: Int): String = getPage(SITE_URL + s"/tournament/$tournamentId/requests/")
}

object Connector {
  val SITE_URL = "https://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class NetWrapper {
  def getPage(url: String): String = Http(url).charset("cp1251").asString.body

  def get(url: String): HttpResponse[String] = Http(url).asString
}