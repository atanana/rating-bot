package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.*
import com.atanana.ratingbot.data.ParsedData
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.net.Connector
import com.atanana.ratingbot.parsers.*
import com.atanana.ratingbot.types.Ids.{TeamId, TournamentId}
import com.typesafe.scalalogging.Logger

import scala.util.chaining.*

class PollingDataProviderImpl(
                               connector: Connector,
                               tournamentInfoProvider: TournamentInfoProvider,
                               lastTeamResultsProvider: LastTeamResultsProvider,
                               config: Config,
                             ) extends PollingDataProvider {

  private val logger = Logger("PollingDataProvider")

  override def data: EitherT[IO, Throwable, ParsedData] =
    for
      newTournaments <- lastTeamResultsProvider.getLastTeamResults(TeamId(config.team))
    yield ParsedData(newTournaments).tap(data => logger.debug(s"Got data $data"))

  private def getQuestionCount(tournamentId: TournamentId): EitherT[IO, Throwable, Int] =
    for
      tournamentInfo <- tournamentInfoProvider.getInfo(tournamentId)
    yield tournamentInfo.questionsCount
}
