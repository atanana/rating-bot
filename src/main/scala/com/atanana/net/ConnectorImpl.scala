package com.atanana.net

import cats.data.EitherT
import com.atanana.json.Config
import com.atanana.net.ConnectorImpl.{API_URL, SITE_URL}
import sttp.client3.*
import sttp.client3.okhttp.OkHttpFutureBackend
import sttp.model.{Header, Uri}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConnectorImpl(netWrapper: NetWrapper, config: Config) extends Connector {

  override def getTeamPage: EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/teams.php?team_id=${config.team}&download_data=export_tournaments"
    getPageAsync(url)
  }

  override def getRequisitionPage: EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/jq_backend/synch.php?upcoming_synch=true&town_id=${config.city}"
    getPageAsync(url)
  }

  private def getPageAsync(uri: Uri): EitherT[Future, Throwable, String] =
    wrapGet(uri, netWrapper.getPageAsync(uri))

  private def wrapGet(uri: Uri, request: Future[Either[String, String]]): EitherT[Future, Throwable, String] =
    EitherT(
      request
        .map(_.left.map(error => new ConnectorException(uri, error)))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )

  override def getTournamentPage(id: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/tournament/$id"
    getPageAsync(url)
  }

  override def getTournamentRequisitionsPage(tournamentId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/tournament/$tournamentId/requests"
    getPageAsync(url)
  }

  override def getTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=&form[releaseId]=$releaseId"
    getPageAsync(url)
  }

  override def getCityTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=${config.city}&form[countryId]=&form[releaseId]=$releaseId"
    getPageAsync(url)
  }

  override def getCountryTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=${config.country}&form[releaseId]=$releaseId"
    getPageAsync(url)
  }

  override def getTournamentInfo(tournamentId: Int): EitherT[Future, Throwable, String] = {
    val url = uri"$API_URL/tournaments/$tournamentId"
    getApiAsync(url)
  }

  override def getReleases: EitherT[Future, Throwable, String] = {
    val url = uri"$API_URL/releases?pagination=false"
    getApiAsync(url)
  }

  override def getTeamTournaments: EitherT[Future, Throwable, String] = {
    val url = uri"$API_URL/teams/${config.team}/tournaments?page=1&itemsPerPage=30&pagination=false"
    getApiAsync(url)
  }

  private def getApiAsync(uri: Uri): EitherT[Future, Throwable, String] =
    wrapGet(uri, netWrapper.getApi(uri))

  override def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String] =
    EitherT(
      netWrapper.postAsync(uri, params)
        .map(_.left.map(error => new ConnectorException(uri, s"$error with params $params")))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )
}

object ConnectorImpl {
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
