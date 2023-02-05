package com.atanana.providers

import cats.data.EitherT
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.data.Release
import com.atanana.parsers.ReleasesParserImpl
import com.atanana.TimeProviderImpl
import com.atanana.mocks.{MockReleasesParser, MockTimeProvider}
import com.atanana.net.{ConnectorImpl, MockConnector}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success
import scala.util.chaining.scalaUtilChainingOps

class ReleasesProviderTest extends AnyWordSpecLike with Matchers {

  private val connector = new MockConnector()
  private val parser = new MockReleasesParser()
  private val timeProvider = new MockTimeProvider()
  private val provider = new ReleasesProviderImpl(connector, parser, timeProvider)

  "ReleasesProvider" should {

    "provide last release id" in {
      connector.releases = EitherT.rightT[Future, Throwable]("releases page")
      parser.releases.put("releases page", Success(List(
        Release(1, LocalDateTime.of(2022, 12, 31, 1, 0)),
        Release(2, LocalDateTime.of(2022, 12, 31, 2, 0)),
        Release(3, LocalDateTime.of(2023, 1, 2, 0, 0)),
      )))
      timeProvider.dateTime = LocalDateTime.of(2023, 1, 1, 0, 0)

      val releaseId = provider.getLastReleaseId.pipe(awaitEither)

      releaseId shouldEqual Right(2)
    }

    "return error if no releases" in {
      connector.releases = EitherT.rightT[Future, Throwable]("releases page")
      parser.releases.put("releases page", Success(List()))

      val error = provider.getLastReleaseId.pipe(awaitError)
      error.getMessage shouldEqual "Cannot find last release!"
    }
  }
}
