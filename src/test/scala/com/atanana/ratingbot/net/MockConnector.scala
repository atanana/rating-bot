package com.atanana.ratingbot.net

import cats.data.EitherT
import com.atanana.ratingbot.net.Connector
import com.atanana.ratingbot.types.Ids.{ReleaseId, TournamentId}
import com.atanana.ratingbot.types.Pages.{TeamTournamentsPage, TournamentInfoPage, TournamentResultsPage}
import sttp.model.Uri

import scala.collection.mutable
import scala.concurrent.Future

class MockConnector extends Connector {

  val postResponses: mutable.Map[(Uri, Map[String, String]), EitherT[Future, Throwable, String]] = mutable.Map()
  val tournamentPageResponses: mutable.Map[TournamentId, EitherT[Future, Throwable, String]] = mutable.Map()
  val teamsPageResponses: mutable.Map[ReleaseId, EitherT[Future, Throwable, String]] = mutable.Map()
  val cityTeamsPageResponses: mutable.Map[ReleaseId, EitherT[Future, Throwable, String]] = mutable.Map()
  val countryTeamsPageResponses: mutable.Map[ReleaseId, EitherT[Future, Throwable, String]] = mutable.Map()
  var releases: EitherT[Future, Throwable, String] = _
  val tournamentInfoResponses: mutable.Map[TournamentId, EitherT[Future, Throwable, TournamentInfoPage]] = mutable.Map()
  val tournamentRequisitionsPageResponses: mutable.Map[TournamentId, EitherT[Future, Throwable, String]] = mutable.Map()
  var requisitionPage: EitherT[Future, Throwable, String] = _
  var teamTournamentsPage: EitherT[Future, Throwable, TeamTournamentsPage] = _
  var tournamentResultsPage: mutable.Map[TournamentId, EitherT[Future, Throwable, TournamentResultsPage]] = mutable.Map()

  override def getRequisitionPage: EitherT[Future, Throwable, String] = requisitionPage

  override def getTournamentPage(id: TournamentId): EitherT[Future, Throwable, String] = tournamentPageResponses(id)

  override def getTournamentRequisitionsPage(tournamentId: TournamentId): EitherT[Future, Throwable, String] =
    tournamentRequisitionsPageResponses(tournamentId)

  override def getTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String] = teamsPageResponses(releaseId)

  override def getCityTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String] = cityTeamsPageResponses(releaseId)

  override def getCountryTeamsPage(releaseId: ReleaseId): EitherT[Future, Throwable, String] = countryTeamsPageResponses(releaseId)

  override def getTournamentInfo(tournamentId: TournamentId): EitherT[Future, Throwable, TournamentInfoPage] = tournamentInfoResponses(tournamentId)

  override def getReleases: EitherT[Future, Throwable, String] = releases

  override def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String] =
    postResponses((uri, params))

  override def getTeamTournaments: EitherT[Future, Throwable, TeamTournamentsPage] = teamTournamentsPage

  override def getTournamentResultsPage(id: TournamentId): EitherT[Future, Throwable, TournamentResultsPage] = tournamentResultsPage(id)
}
