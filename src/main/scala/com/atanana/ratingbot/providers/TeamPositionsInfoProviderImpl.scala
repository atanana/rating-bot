package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.data.TeamPositionsInfo
import com.atanana.ratingbot.net.{Connector, ConnectorImpl}
import com.atanana.ratingbot.parsers.{TeamsPageParser, TeamsPageParserImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamPositionsInfoProviderImpl(
                                     connector: Connector,
                                     parser: TeamsPageParser,
                                     composer: TeamPositionsInfoComposer,
                                     releasesProvider: ReleasesProvider
                                   ) extends TeamPositionsInfoProvider {

  override def data: EitherT[Future, Throwable, TeamPositionsInfo] = for
    releaseId <- releasesProvider.getLastReleaseId
    allTeams <- connector.getTeamsPage(releaseId).map(parser.getTeams)
    cityTeams <- connector.getCityTeamsPage(releaseId).map(parser.getTeams)
    countryTeams <- connector.getCountryTeamsPage(releaseId).map(parser.getTeams)

    positionsInfo <- EitherT.fromEither[Future](composer.positionsInfo(allTeams, cityTeams, countryTeams)
      .left.map[Throwable](new RuntimeException(_)))
  yield positionsInfo
}
