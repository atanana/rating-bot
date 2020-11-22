package com.atanana

import com.atanana.Connector.SITE_URL
import com.atanana.json.Config
import javax.inject.Inject
import sttp.client3._
import sttp.model.Uri

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {

  def post(uri: Uri, params: Map[String, String]): Either[String, String] = netWrapper.post(uri, params)

  private def getPage(uri: Uri) = netWrapper.getPage(uri)

  private def getPageSafe(uri: Uri) = netWrapper.getPageSafe(uri)

  def getTeamPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
    getPageSafe(url).left.map(error => s"Cannot get team's page($url): $error")
  }

  def getTournamentPage(id: Int): Either[String, String] = {
    val url = uri"$SITE_URL/tournament/$id"
    getPageSafe(url).left.map(error => s"Cannot get tournament's page($url): $error")
  }

  def getRequisitionPage: Either[String, String] = {
    val url = uri"$SITE_URL/synch_town/${config.city}"
    getPageSafe(url).left.map(error => s"Cannot get requisitions page($url): $error")
  }

  def getTeamsPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php"
    getPageSafe(url).left.map(error => s"Cannot get teams page($url): $error")
  }

  def getCityTeamsPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php?town=${config.cityName}"
    getPageSafe(url).left.map(error => s"Cannot get city teams page($url): $error")
  }

  def getCountryTeamsPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php?country=${config.countryName}"
    getPageSafe(url).left.map(error => s"Cannot get country teams page($url): $error")
  }

  def getTournamentRequisitionsPage(tournamentId: Int): Either[String, String] = {
    val url = uri"$SITE_URL/tournament/$tournamentId/requests"
    getPageSafe(url).left.map(error => s"Cannot get tournament requisitions page($url): $error")
  }

  def getTournamentInfo(tournamentId: Int): Either[String, String] = {
    val url = uri"$SITE_URL/api/tournaments/$tournamentId.json"
    getPageSafe(url).left.map(error => s"Cannot get tournament info page($url): $error")
  }
}

object Connector {
  val SITE_URL = "https://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class NetWrapper {
  private val backend = HttpURLConnectionBackend()

  def getPageSafe(uri: Uri): Either[String, String] =
    basicRequest
      .get(uri)
      .send(backend)
      .body

  def getPage(uri: Uri): String = getPageSafe(uri).right.get

  def post(uri: Uri, params: Map[String, String]): Either[String, String] =
    basicRequest
      .body(params)
      .post(uri)
      .send(backend)
      .body
}