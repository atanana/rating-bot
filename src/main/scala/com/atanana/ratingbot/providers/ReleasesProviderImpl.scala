package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.data.Release
import com.atanana.ratingbot.net.{Connector, ConnectorImpl}
import com.atanana.ratingbot.parsers.{ReleasesParser, ReleasesParserImpl}
import com.atanana.ratingbot.types.Ids.ReleaseId
import com.atanana.ratingbot.{TimeProvider, TimeProviderImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReleasesProviderImpl(connector: Connector, parser: ReleasesParser, timeProvider: TimeProvider) extends ReleasesProvider {

  override def getLastReleaseId: EitherT[Future, Throwable, ReleaseId] = for
    releasesPage <- connector.getReleases
    lastRelease <- EitherT.fromEither[Future](processReleasesPage(releasesPage))
  yield lastRelease.id

  private def processReleasesPage(releasesPage: String): Either[Throwable, Release] = for
    releases <- parser.getReleases(releasesPage).toEither
    lastRelease <- selectLastRelease(releases)
  yield lastRelease

  private def selectLastRelease(releases: List[Release]): Either[Throwable, Release] = {
    val now = timeProvider.now
    releases.findLast(_.date.isBefore(now)).toRight(new RuntimeException("Cannot find last release!"))
  }
}
