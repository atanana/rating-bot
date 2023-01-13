package com.atanana.providers

import cats.data.EitherT
import com.atanana.Connector
import com.atanana.data.TeamPositionsInfo
import com.atanana.parsers.TeamsPageParser

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamPositionsInfoProvider @Inject()(
                                           connector: Connector,
                                           parser: TeamsPageParser,
                                           composer: TeamPositionsInfoComposer,
                                           releasesProvider: ReleasesProvider
                                         ) {

  def data: EitherT[Future, Throwable, TeamPositionsInfo] = for {
    releaseId <- releasesProvider.getLastReleaseId
    allTeams <- connector.getTeamsPage(releaseId).map(parser.getTeams)
    cityTeams <- connector.getCityTeamsPage(releaseId).map(parser.getTeams)
    countryTeams <- connector.getCountryTeamsPage(releaseId).map(parser.getTeams)

    positionsInfo <- EitherT.fromEither[Future](composer.positionsInfo(allTeams, cityTeams, countryTeams)
      .left.map[Throwable](new RuntimeException(_)))
  } yield positionsInfo
}
