package com.atanana.net

import cats.data.EitherT
import com.atanana.types.Pages.{TeamTournamentsPage, TournamentResultsPage}
import sttp.model.Uri

import scala.concurrent.Future

trait Connector {

  def getTeamPage: EitherT[Future, Throwable, String]

  def getTeamTournaments: EitherT[Future, Throwable, TeamTournamentsPage]

  def getTournamentResultsPage(tournamentId: Int): EitherT[Future, Throwable, TournamentResultsPage]

  def getRequisitionPage: EitherT[Future, Throwable, String]

  def getTournamentPage(id: Int): EitherT[Future, Throwable, String]

  def getTournamentRequisitionsPage(tournamentId: Int): EitherT[Future, Throwable, String]

  def getTeamsPage(releaseId: Int): EitherT[Future, Throwable, String]

  def getCityTeamsPage(releaseId: Int): EitherT[Future, Throwable, String]

  def getCountryTeamsPage(releaseId: Int): EitherT[Future, Throwable, String]

  def getTournamentInfo(tournamentId: Int): EitherT[Future, Throwable, String]

  def getReleases: EitherT[Future, Throwable, String]

  def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String]
}
