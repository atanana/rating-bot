package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.TeamPositionsInfo
import com.atanana.ratingbot.net.Connector
import com.atanana.ratingbot.parsers.TeamsPageParser

class TeamPositionsInfoProviderImpl(
                                     connector: Connector,
                                     parser: TeamsPageParser,
                                     composer: TeamPositionsInfoComposer,
                                     releasesProvider: ReleasesProvider
                                   ) extends TeamPositionsInfoProvider {

  override def data: EitherT[IO, Throwable, TeamPositionsInfo] = for
    releaseId <- releasesProvider.getLastReleaseId
    allTeams <- connector.getTeamsPage(releaseId).map(parser.getTeams)
    cityTeams <- connector.getCityTeamsPage(releaseId).map(parser.getTeams)
    countryTeams <- connector.getCountryTeamsPage(releaseId).map(parser.getTeams)

    positionsInfo <- EitherT.fromEither(composer.positionsInfo(allTeams, cityTeams, countryTeams)
      .left.map[Throwable](new RuntimeException(_)))
  yield positionsInfo
}
