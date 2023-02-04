package com.atanana.net

import cats.data.EitherT
import sttp.model.Uri

import scala.collection.mutable
import scala.concurrent.Future

class MockConnector extends Connector {

  val postResponses: mutable.Map[(Uri, Map[String, String]), EitherT[Future, Throwable, String]] = mutable.Map[(Uri, Map[String, String]), EitherT[Future, Throwable, String]]()
  val tournamentPageResponses: mutable.Map[Int, EitherT[Future, Throwable, String]] = mutable.Map[Int, EitherT[Future, Throwable, String]]()

  override def getTeamPage: EitherT[Future, Throwable, String] = ???

  override def getRequisitionPage: EitherT[Future, Throwable, String] = ???

  override def getTournamentPage(id: Int): EitherT[Future, Throwable, String] = tournamentPageResponses(id)

  override def getTournamentRequisitionsPage(tournamentId: Int): EitherT[Future, Throwable, String] = ???

  override def getTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = ???

  override def getCityTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = ???

  override def getCountryTeamsPage(releaseId: Int): EitherT[Future, Throwable, String] = ???

  override def getTournamentInfo(tournamentId: Int): EitherT[Future, Throwable, String] = ???

  override def getReleases: EitherT[Future, Throwable, String] = ???

  override def postAsync(uri: Uri, params: Map[String, String]): EitherT[Future, Throwable, String] =
    postResponses((uri, params))
}
