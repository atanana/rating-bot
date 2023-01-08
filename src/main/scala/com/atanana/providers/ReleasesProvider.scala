package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.Release
import com.atanana.parsers.ReleasesParser
import com.atanana.{Connector, TimeProvider}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReleasesProvider @Inject()(connector: Connector, parser: ReleasesParser, timeProvider: TimeProvider) {

  def getLastReleaseId: EitherT[Future, Throwable, Int] = for {
    releasesPage <- connector.getReleases
    lastRelease <- EitherT.fromEither[Future](processReleasesPage(releasesPage))
  } yield lastRelease.id

  private def processReleasesPage(releasesPage: String): Either[Throwable, Release] = for {
    releases <- parser.getReleases(releasesPage).toEither
    lastRelease <- selectLastRelease(releases)
  } yield lastRelease

  private def selectLastRelease(releases: List[Release]): Either[Throwable, Release] = {
    val now = timeProvider.now
    releases.findLast(_.date.isBefore(now)).toRight(new RuntimeException("Cannot find last release!"))
  }
}
