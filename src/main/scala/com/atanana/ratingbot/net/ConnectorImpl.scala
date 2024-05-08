package com.atanana.ratingbot.net

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.types.Ids.{ReleaseId, TeamId, TournamentId}
import com.atanana.ratingbot.types.Pages.{TeamTournamentsPage, TournamentInfoPage, TournamentResultsPage}
import sttp.client3.*
import sttp.model.Uri

class ConnectorImpl(netWrapper: NetWrapper, config: Config) extends Connector {

  override def getRequisitionPage: EitherT[IO, Throwable, String] = {
    val url = UriComposer.requisitionPageUri(config.city)
    getPageAsync(url)
  }

  private def getPageAsync(uri: Uri): EitherT[IO, Throwable, String] =
    wrapGet(uri, netWrapper.getPageAsync(uri))

  override def getTournamentPage(id: TournamentId): EitherT[IO, Throwable, String] = {
    val url = UriComposer.tournamentPageUri(id)
    getPageAsync(url)
  }

  override def getTournamentRequisitionsPage(tournamentId: TournamentId): EitherT[IO, Throwable, String] = {
    val url = UriComposer.tournamentRequisitionsPageUri(tournamentId)
    getPageAsync(url)
  }

  override def getTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String] = {
    val url = UriComposer.teamsPageUri(releaseId)
    getPageAsync(url)
  }

  override def getCityTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String] = {
    val url = UriComposer.cityTeamsPageUri(releaseId, config.city)
    getPageAsync(url)
  }

  override def getCountryTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String] = {
    val url = UriComposer.countryTeamsPageUri(releaseId, config.country)
    getPageAsync(url)
  }

  override def getTournamentInfo(tournamentId: TournamentId): EitherT[IO, Throwable, TournamentInfoPage] = {
    val url = UriComposer.tournamentInfoUri(tournamentId)
    getApiAsync(url).map(TournamentInfoPage(_))
  }

  override def getReleases: EitherT[IO, Throwable, String] = {
    val url = UriComposer.releasesUri
    getApiAsync(url)
  }

  override def getTeamTournaments: EitherT[IO, Throwable, TeamTournamentsPage] = {
    val url = UriComposer.teamTournamentsUri(TeamId(config.team)) //todo
    getApiAsync(url).map(TeamTournamentsPage(_))
  }

  private def getApiAsync(uri: Uri): EitherT[IO, Throwable, String] =
    wrapGet(uri, netWrapper.getApi(uri))

  private def wrapGet(uri: Uri, request: IO[Either[String, String]]): EitherT[IO, Throwable, String] =
    EitherT(
      request
        .map(_.left.map(error => new ConnectorException(uri, error)))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )

  override def getTournamentResultsPage(id: TournamentId): EitherT[IO, Throwable, TournamentResultsPage] = {
    val url = UriComposer.tournamentResultsUri(id)
    getApiAsync(url).map(TournamentResultsPage(_))
  }

  override def postAsync(uri: Uri, params: Map[String, String]): EitherT[IO, Throwable, String] =
    EitherT(
      netWrapper.postAsync(uri, params)
        .map(_.left.map(error => new ConnectorException(uri, s"$error with params $params")))
        .recover(exception => Left(new ConnectorException(uri, cause = exception)))
    )
}

class ConnectorException(
                          val uri: Uri,
                          message: String = null,
                          cause: Throwable = null
                        ) extends RuntimeException(message, cause) {
  override def getMessage: String = s"Error uri: $uri\n${super.getMessage}"
}
