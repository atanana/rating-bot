package com.atanana.ratingbot.net

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.types.Ids.{ReleaseId, TournamentId}
import com.atanana.ratingbot.types.Pages.{TeamTournamentsPage, TournamentInfoPage, TournamentResultsPage}
import sttp.model.Uri

trait Connector {

  def getTeamTournaments: EitherT[IO, Throwable, TeamTournamentsPage]

  def getTournamentResultsPage(id: TournamentId): EitherT[IO, Throwable, TournamentResultsPage]

  def getRequisitionPage: EitherT[IO, Throwable, String]

  def getTournamentPage(id: TournamentId): EitherT[IO, Throwable, String]

  def getTournamentRequisitionsPage(tournamentId: TournamentId): EitherT[IO, Throwable, String]

  def getTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String]

  def getCityTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String]

  def getCountryTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String]

  def getTournamentInfo(tournamentId: TournamentId): EitherT[IO, Throwable, TournamentInfoPage]

  def getReleases: EitherT[IO, Throwable, String]

  def postAsync(uri: Uri, params: Map[String, String]): EitherT[IO, Throwable, String]
}
