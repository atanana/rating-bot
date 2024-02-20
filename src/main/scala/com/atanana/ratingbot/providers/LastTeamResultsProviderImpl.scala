package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.implicits.*
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.net.Connector
import com.atanana.ratingbot.parsers.{TeamTournamentsParser, TournamentResultsParser}
import com.atanana.ratingbot.types.Ids.{TeamId, TournamentId}
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LastTeamResultsProviderImpl(
                                   connector: Connector,
                                   teamTournamentsParser: TeamTournamentsParser,
                                   tournamentResultsParser: TournamentResultsParser
                                 ) extends LastTeamResultsProvider {

  private val logger = Logger("LastTeamResultsProvider")

  override def getLastTeamResults(teamId: TeamId): EitherT[Future, Throwable, Set[TournamentResult]] = for
    teamTournamentsPage <- connector.getTeamTournaments
    tournamentIds <- EitherT.fromEither[Future](teamTournamentsParser.getTournamentIds(teamTournamentsPage).toEither)
    lastTournaments = tournamentIds.toList.sorted(TournamentId.ordering.reverse).take(30)
    results <- lastTournaments.traverse(tournamentId => getTournamentResult(tournamentId, teamId))
  yield results.flatMap(_.toList).toSet

  private def getTournamentResult(tournamentId: TournamentId, teamId: TeamId): EitherT[Future, Throwable, Option[TournamentResult]] = for
    page <- connector.getTournamentResultsPage(tournamentId)
  yield tournamentResultsParser.getTeamResults(page, tournamentId, teamId).getOrElse {
    logger.warn(s"Cannot get results on $tournamentId for $teamId")
    None
  }
}
