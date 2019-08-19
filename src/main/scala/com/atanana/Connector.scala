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

import scala.collection.JavaConverters._
import scala.io.Source

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {
  def get(url: String): HttpResponse[String] = netWrapper.get(url)

  def post(url: String, params: Map[String, String]): String = netWrapper.post(url, params)

  def getTeamPage: String = getPage(teamUrl)

  private def teamUrl: String = {
    SITE_URL + s"/teams.php?team_id=${config.team}&download_data=export_tournaments"
  }

  private def getPage(url: String) = netWrapper.getPage(url)

  def getTournamentPage(id: Int): String = getPage(tournamentUrl(id))

  private def tournamentUrl(id: Int): String = SITE_URL + "/tournament/" + id

  def getRequisitionPage: String = getPage(requisitionUrl)

  private def requisitionUrl: String = SITE_URL + "/synch_town/" + config.city

  def getTeamsPage: String = getPage(SITE_URL + "/teams.php")

  def getCityTeamsPage: String = getPage(SITE_URL + s"/teams.php?town=${encode(config.cityName, "UTF-8")}")

  def getCountryTeamsPage: String = getPage(SITE_URL + s"/teams.php?country=${encode(config.countryName, "UTF-8")}")

  def getTournamentRequisitionsPage(tournamentId: Int): String = getPage(SITE_URL + s"/tournament/$tournamentId/requests/")

  def getTournamentInfo(tournamentId: Int): String = getPage(SITE_URL + s"/api/tournaments/$tournamentId.json")
}

object Connector {
  val SITE_URL = "https://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class NetWrapper {
  private val client = HttpClientBuilder.create().build()

  def getPage(url: String): String = Http(url).charset("UTF-8").asString.body

  def get(url: String): HttpResponse[String] = Http(url).asString

  def post(url: String, params: Map[String, String]): String = {
    val request = new HttpPost(url)
    val valuePairs = params.toList.map({ case (key, value) => new BasicNameValuePair(key, value) }).asJava
    request.setEntity(new UrlEncodedFormEntity(valuePairs, Charsets.UTF_8))
    val response: CloseableHttpResponse = client.execute(request)
    Source.fromInputStream(response.getEntity.getContent).mkString
  }
}