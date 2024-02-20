package com.atanana.providers

import cats.data.EitherT
import cats.implicits.*
import com.atanana.data.TournamentResult
import com.atanana.net.Connector
import com.atanana.parsers.{TeamTournamentsParser, TournamentResultsParser}
import com.atanana.types.Ids.{TeamId, TournamentId}
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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
