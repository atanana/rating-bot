package com.atanana

import cats.data.EitherT
import com.atanana.Connector.SITE_URL
import com.atanana.json.Config
import sttp.client3._
import sttp.client3.httpclient.HttpClientFutureBackend
import sttp.model.Uri

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {

  def post(uri: Uri, params: Map[String, String]): Either[String, String] =
    netWrapper.post(uri, params).left.map(error => s"Cannot post $params to $uri: $error")

  def postAsync(uri: Uri, params: Map[String, String]): Future[Either[String, String]] =
    netWrapper.postAsync(uri, params).map(_.left.map(error => s"Cannot post $params to $uri: $error"))

  private def getPage(uri: Uri) = netWrapper.getPage(uri)

  def getTeamPage: EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
    getPageAsync(url)
  }

  def getRequisitionPage: EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/synch_town/${config.city}"
    getPageAsync(url)
  }

  def getTournamentPage(id: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/tournament/$id"
    getPageAsync(url)
  }

  def getTournamentRequisitionsPage(tournamentId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/tournament/$tournamentId/requests"
    getPageAsync(url)
  }

  def getTeamsPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php"
    getPage(url).left.map(error => s"Cannot get teams page($url): $error")
  }

  def getCityTeamsPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php?town=${config.cityName}"
    getPage(url).left.map(error => s"Cannot get city teams page($url): $error")
  }

  def getCountryTeamsPage: Either[String, String] = {
    val url = uri"$SITE_URL/teams.php?country=${config.countryName}"
    getPage(url).left.map(error => s"Cannot get country teams page($url): $error")
  }

  def getTournamentInfo(tournamentId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/api/tournaments/$tournamentId.json"
    getPageAsync(url)
  }

  private def getPageAsync(uri: Uri): EitherT[Future, Throwable, String] =
    EitherT(
      netWrapper.getPageAsync(uri)
        .map(_.left.map(error => new ConnectorException(uri, error)))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )
}

object Connector {
  val SITE_URL = "https://rating.chgk.info"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class ConnectorException(
                          val uri: Uri,
                          message: String = null,
                          cause: Throwable = null
                        ) extends RuntimeException(message, cause)

class NetWrapper {
  private val backend = HttpURLConnectionBackend()
  private val asyncBackend = HttpClientFutureBackend()

  def getPage(uri: Uri): Either[String, String] =
    basicRequest
      .get(uri)
      .send(backend)
      .body

  def getPageAsync(uri: Uri): Future[Either[String, String]] =
    basicRequest
      .get(uri)
      .send(asyncBackend)
      .map(_.body)

  def post(uri: Uri, params: Map[String, String]): Either[String, String] =
    basicRequest
      .body(params)
      .post(uri)
      .send(backend)
      .body

  def postAsync(uri: Uri, params: Map[String, String]): Future[Either[String, String]] =
    basicRequest
      .body(params)
      .post(uri)
      .send(asyncBackend)
      .map(_.body)
}