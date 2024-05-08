package com.atanana.ratingbot.net

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.net.Connector
import com.atanana.ratingbot.types.Ids.{ReleaseId, TournamentId}
import com.atanana.ratingbot.types.Pages.{TeamTournamentsPage, TournamentInfoPage, TournamentResultsPage}
import sttp.model.Uri

import scala.collection.mutable

class MockConnector extends Connector {

  val postResponses: mutable.Map[(Uri, Map[String, String]), EitherT[IO, Throwable, String]] = mutable.Map()
  val tournamentPageResponses: mutable.Map[TournamentId, EitherT[IO, Throwable, String]] = mutable.Map()
  val teamsPageResponses: mutable.Map[ReleaseId, EitherT[IO, Throwable, String]] = mutable.Map()
  val cityTeamsPageResponses: mutable.Map[ReleaseId, EitherT[IO, Throwable, String]] = mutable.Map()
  val countryTeamsPageResponses: mutable.Map[ReleaseId, EitherT[IO, Throwable, String]] = mutable.Map()
  val tournamentInfoResponses: mutable.Map[TournamentId, EitherT[IO, Throwable, TournamentInfoPage]] = mutable.Map()
  val tournamentRequisitionsPageResponses: mutable.Map[TournamentId, EitherT[IO, Throwable, String]] = mutable.Map()
  var releases: EitherT[IO, Throwable, String] = _
  var requisitionPage: EitherT[IO, Throwable, String] = _
  var teamTournamentsPage: EitherT[IO, Throwable, TeamTournamentsPage] = _
  var tournamentResultsPage: mutable.Map[TournamentId, EitherT[IO, Throwable, TournamentResultsPage]] = mutable.Map()

  override def getRequisitionPage: EitherT[IO, Throwable, String] = requisitionPage

  override def getTournamentPage(id: TournamentId): EitherT[IO, Throwable, String] = tournamentPageResponses(id)

  override def getTournamentRequisitionsPage(tournamentId: TournamentId): EitherT[IO, Throwable, String] =
    tournamentRequisitionsPageResponses(tournamentId)

  override def getTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String] = teamsPageResponses(releaseId)

  override def getCityTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String] = cityTeamsPageResponses(releaseId)

  override def getCountryTeamsPage(releaseId: ReleaseId): EitherT[IO, Throwable, String] = countryTeamsPageResponses(releaseId)

  override def getTournamentInfo(tournamentId: TournamentId): EitherT[IO, Throwable, TournamentInfoPage] = tournamentInfoResponses(tournamentId)

  override def getReleases: EitherT[IO, Throwable, String] = releases

  override def postAsync(uri: Uri, params: Map[String, String]): EitherT[IO, Throwable, String] =
    postResponses((uri, params))

  override def getTeamTournaments: EitherT[IO, Throwable, TeamTournamentsPage] = teamTournamentsPage

  override def getTournamentResultsPage(id: TournamentId): EitherT[IO, Throwable, TournamentResultsPage] = tournamentResultsPage(id)
}
