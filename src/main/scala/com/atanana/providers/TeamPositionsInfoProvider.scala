package com.atanana.providers

import cats.data.EitherT
import com.atanana.Connector
import com.atanana.data.{Team, TeamPositionsInfo}
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
    countryTeams <- connector.getCountryTeamsPage.map(parser.getTeams)

    positionsInfo <- EitherT.fromEither[Future](composer.positionsInfo(
      teams = filter(allTeams),
      cityTeams = filter(cityTeams),
      countryTeams = filter(countryTeams)
    ).left.map[Throwable](new RuntimeException(_)))
  } yield positionsInfo

  private def filter(teams: List[Team]): List[Team] = teams.filter(_.isReal)
}
