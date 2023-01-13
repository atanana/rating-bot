package com.atanana

import cats.data.EitherT
import com.atanana.Connector.{API_URL, SITE_URL}
import com.atanana.json.Config
import sttp.client3._
import sttp.client3.okhttp.OkHttpFutureBackend
import sttp.model.{Header, Uri}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Connector @Inject()(netWrapper: NetWrapper, config: Config) {

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

  def getTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=&form[releaseId]=$releaseId"
    getPageAsync(url)
  }

  def getCityTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=${config.city}&form[countryId]=&form[releaseId]=$releaseId"
    getPageAsync(url)
  }

  def getCountryTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=${config.country}&form[releaseId]=$releaseId"
    getPageAsync(url)
  }

  def getTournamentInfo(tournamentId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$API_URL/tournaments/$tournamentId"
    getApiAsync(url)
  }

  def getReleases: EitherT[Future, Throwable, String] = {
    val url = uri"$API_URL/releases?pagination=false"
    getApiAsync(url)
  }

  private def getApiAsync(uri: Uri): EitherT[Future, Throwable, String] =
    wrapGet(uri, netWrapper.getApi(uri))

  private def getPageAsync(uri: Uri): EitherT[Future, Throwable, String] =
    wrapGet(uri, netWrapper.getPageAsync(uri))

  private def wrapGet(uri: Uri, request: Future[Either[String, String]]): EitherT[Future, Throwable, String] =
    EitherT(
      request
        .map(_.left.map(error => new ConnectorException(uri, error)))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )

  def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String] =
    EitherT(
      netWrapper.postAsync(uri, params)
        .map(_.left.map(error => new ConnectorException(uri, s"$error with params $params")))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )
}

object Connector {
  val SITE_URL = "https://rating.chgk.info"
  val API_URL = "https://api.rating.chgk.net"
  val TOURNAMENT_URL_TEMPLATE: String = SITE_URL + "/tournament/"
}

class ConnectorException(
                          val uri: Uri,
                          message: String = null,
                          cause: Throwable = null
                        ) extends RuntimeException(message, cause) {
  override def getMessage: String = s"Error uri: $uri\n${super.getMessage}"
}

class NetWrapper @Inject()(config: Config) {

  private val asyncBackend = OkHttpFutureBackend()
  private val authCookie = ("REMEMBERME", config.authCookie)
  private val apiAuthHeader = Header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2NjQ3MTgyODAsImV4cCI6MTY2NDcyMTg4MCwicm9sZXMiOltdLCJ1c2VybmFtZSI6InRhbmFuYS5hbmRyZXlAZ21haWwuY29tIn0.RkhxOTysbw5Hn3rysEU2kRUrnM2DuPkmQkTDzWiQNqA6ehYFFtCLyI6GX-7kwSuHvhiLnYdiv6IREw9QwPxO3wsT4tjNALG05NWpxNLnJQjLt85Y5aaQ1f3vc5QZ7kfCkJiNlPSz-YES_03kCdmym1rTLqM1K2jBPbV7qlRbBRefH8c1aXE7mhx9qZHXVgXbTYcA8-9wGyZ_4VZa8cwLXOhYBmZtQedoBrCP6rk8LEC1VgyJD4hWd2x6oOsajf-tHK3F8zi6UuxG7LLejQ-eAo_EUi8F0UOIRXnUiy4f6JYkHoX8szNUaCoxFlT6qWVg1Yd7WQgD3NN3KVM8f0li0A")

  def getPageAsync(uri: Uri): Future[Either[String, String]] =
    basicRequest
      .get(uri)
      .cookie(authCookie)
      .send(asyncBackend)
      .map(_.body)

  def postAsync(uri: Uri, params: Map[String, String]): Future[Either[String, String]] =
    basicRequest
      .body(params)
      .post(uri)
      .cookie(authCookie)
      .send(asyncBackend)
      .map(_.body)

  def getApi(uri: Uri): Future[Either[String, String]] =
    basicRequest
      .get(uri)
      .header(apiAuthHeader)
      .send(asyncBackend)
      .map(_.body)
}