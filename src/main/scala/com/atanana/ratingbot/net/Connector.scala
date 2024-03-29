package com.atanana.ratingbot.net

import cats.data.EitherT
import com.atanana.ratingbot.types.Ids.{ReleaseId, TournamentId}
import com.atanana.ratingbot.types.Pages.{TeamTournamentsPage, TournamentInfoPage, TournamentResultsPage}
import sttp.model.Uri

import scala.concurrent.Future

trait Connector {

  def getTeamTournaments: EitherT[Future, Throwable, TeamTournamentsPage]

  def getTournamentResultsPage(id: TournamentId): EitherT[Future, Throwable, TournamentResultsPage]

  def getRequisitionPage: EitherT[Future, Throwable, String]

  def getTournamentPage(id: TournamentId): EitherT[Future, Throwable, String]

  def getTournamentRequisitionsPage(tournamentId: TournamentId): EitherT[Future, Throwable, String]

  def getTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String]

  def getCityTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String]

  def getCountryTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String]

  def getTournamentInfo(tournamentId: TournamentId): EitherT[Future, Throwable, TournamentInfoPage]

  def getReleases: EitherT[Future, Throwable, String]

  def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String]
}
