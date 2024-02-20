package com.atanana.net

import cats.data.EitherT
import com.atanana.json.Config
import com.atanana.types.Ids.{ReleaseId, TeamId, TournamentId}
import com.atanana.types.Pages.{TeamTournamentsPage, TournamentInfoPage, TournamentResultsPage}
import sttp.client3.*
import sttp.client3.okhttp.OkHttpFutureBackend
import sttp.model.{Header, Uri}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConnectorImpl(netWrapper: NetWrapper, config: Config) extends Connector {

  override def getRequisitionPage: EitherT[Future, Throwable, String] = {
    val url = UriComposer.requisitionPageUri(config.city)
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

  override def getTournamentPage(id: TournamentId): EitherT[Future, Throwable, String] = {
    val url = UriComposer.tournamentPageUri(id)
    getPageAsync(url)
  }

  override def getTournamentRequisitionsPage(tournamentId: TournamentId): EitherT[Future, Throwable, String] = {
    val url = UriComposer.tournamentRequisitionsPageUri(tournamentId)
    getPageAsync(url)
  }

  override def getTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String] = {
    val url = UriComposer.teamsPageUri(releaseId)
    getPageAsync(url)
  }

  override def getCityTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String] = {
    val url = UriComposer.cityTeamsPageUri(releaseId, config.city)
    getPageAsync(url)
  }

  override def getCountryTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String] = {
    val url = UriComposer.countryTeamsPageUri(releaseId, config.country)
    getPageAsync(url)
  }

  override def getTournamentInfo(tournamentId: TournamentId): EitherT[Future, Throwable, TournamentInfoPage] = {
    val url = UriComposer.tournamentInfoUri(tournamentId)
    getApiAsync(url).map(TournamentInfoPage(_))
  }

  override def getReleases: EitherT[Future, Throwable, String] = {
    val url = UriComposer.releasesUri
    getApiAsync(url)
  }

  override def getTeamTournaments: EitherT[Future, Throwable, TeamTournamentsPage] = {
    val url = UriComposer.teamTournamentsUri(TeamId(config.team)) //todo
    getApiAsync(url).map(TeamTournamentsPage(_))
  }

  override def getTournamentResultsPage(id: TournamentId): EitherT[Future, Throwable, TournamentResultsPage] = {
    val url = UriComposer.tournamentResultsUri(id)
    getApiAsync(url).map(TournamentResultsPage(_))
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
