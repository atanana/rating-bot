package com.atanana.net

import cats.data.EitherT
import com.atanana.types.Pages.{TeamTournamentsPage, TournamentResultsPage}
import sttp.model.Uri

import scala.collection.mutable
import scala.concurrent.Future

class MockConnector extends Connector {

  val postResponses: mutable.Map[(Uri, Map[String, String]), EitherT[Future, Throwable, String]] = mutable.Map()
  val tournamentPageResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map()
  val teamsPageResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map()
  val cityTeamsPageResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map()
  val countryTeamsPageResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map()
  var releases: EitherT[Future, Throwable, String] = _
  val tournamentInfoResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map()
  val tournamentRequisitionsPageResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map()
  var requisitionPage: EitherT[Future, Throwable, String] = _
  var teamPage: EitherT[Future, Throwable, String] = _
  var teamTournamentsPage: EitherT[Future, Throwable, TeamTournamentsPage] = _
  var tournamentResultsPage: mutable.Map[Int, EitherT[Future, Throwable, TournamentResultsPage]] = mutable.Map()

  override def getTeamPage: EitherT[Future, Throwable, String] = teamPage

  override def getRequisitionPage: EitherT[Future, Throwable, String] = requisitionPage

  override def getTournamentPage(id: Int): EitherT[Future, Throwable, String] = tournamentPageResponses(id)

  override def getTournamentRequisitionsPage(tournamentId: Int): EitherT[Future, Throwable, String] =
    tournamentRequisitionsPageResponses(tournamentId)

  override def getTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = teamsPageResponses(releaseId)

  override def getCityTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = cityTeamsPageResponses(releaseId)

  override def getCountryTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = countryTeamsPageResponses(releaseId)

  override def getTournamentInfo(tournamentId: Int): EitherT[Future, Throwable, String] = tournamentInfoResponses(tournamentId)

  override def getReleases: EitherT[Future, Throwable, String] = releases

  override def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String] =
    postResponses((uri, params))

  override def getTeamTournaments: EitherT[Future, Throwable, TeamTournamentsPage] = teamTournamentsPage

  override def getTournamentResultsPage(tournamentId: Int): EitherT[Future, Throwable, TournamentResultsPage] = tournamentResultsPage(tournamentId)
}
