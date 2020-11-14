package com.atanana

import java.net.URLEncoder.encode

import com.atanana.Connector.SITE_URL
import com.atanana.json.Config
import com.google.common.base.Charsets
import javax.inject.Inject
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import scalaj.http.{Http, HttpResponse}
import sttp.client3._
import sttp.model.Uri

import scala.collection.JavaConverters._
import scala.io.Source

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {
  def get(url: String): HttpResponse[String] = netWrapper.get(url)

  def post(url: String, params: Map[String, String]): String = netWrapper.post(url, params)

  private val teamUrl = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"

  def getTeamPage: String = getPage(teamUrl)

  private def getPage(uri: Uri) = netWrapper.getPage(uri)

  def getTournamentPage(id: Int): String = getPage(tournamentUrl(id))

  private def tournamentUrl(id: Int) = uri"$SITE_URL/tournament/$id"

  private def requisitionUrl = uri"$SITE_URL/synch_town/${config.city}"

  def getRequisitionPage: String = getPage(requisitionUrl)

  private val teamsUrl = uri"$SITE_URL/teams.php"

  def getTeamsPage: String = getPage(teamsUrl)

  private val cityTeamsUrl = uri"$SITE_URL/teams.php?town=${config.cityName}"

  def getCityTeamsPage: String = getPage(cityTeamsUrl)

  private val countryTeamsUrl = uri"$SITE_URL/teams.php?country=${config.countryName}"

  def getCountryTeamsPage: String = getPage(countryTeamsUrl)

  def getTournamentRequisitionsPage(tournamentId: Int): String = getPage(uri"$SITE_URL/tournament/$tournamentId/requests")

  def getTournamentInfo(tournamentId: Int): String = getPage(uri"$SITE_URL/api/tournaments/$tournamentId.json")
}

object Connector {
  val SITE_URL = "https://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class NetWrapper {
  private val client = HttpClientBuilder.create().build()
  private val backend = HttpURLConnectionBackend()

  def getPage(uri: Uri): String = basicRequest.get(uri).send(backend).body.toOption.get

  def get(url: String): HttpResponse[String] = Http(url).asString

  def post(url: String, params: Map[String, String]): String = {
    val request = new HttpPost(url)
    val valuePairs = params.toList.map({ case (key, value) => new BasicNameValuePair(key, value) }).asJava
    request.setEntity(new UrlEncodedFormEntity(valuePairs, Charsets.UTF_8))
    val response: CloseableHttpResponse = client.execute(request)
    Source.fromInputStream(response.getEntity.getContent).mkString
  }
}