package com.atanana

import cats.data.EitherT
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.data.Editor
import com.atanana.mocks.MockTournamentPageParser
import com.atanana.net.MockConnector
import com.atanana.parsers.TournamentPageParserImpl
import com.atanana.providers.TournamentInfoProviderImpl
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class TournamentInfoProviderTest extends AnyWordSpecLike with Matchers {
  private val connector = new MockConnector()
  private val parser = new MockTournamentPageParser()

  private val provider = new TournamentInfoProviderImpl(connector, parser)

  "TournamentInfoProvider" should {

    "provide correct info" in {
      val tournamentId = 123
      val page = "tournament page"
      val editor = Editor("test")
      connector.tournamentPageResponses.put(tournamentId, EitherT.rightT[Future, Throwable](page))
      parser.editors = List(editor)

      provider.getEditors(tournamentId).pipe(awaitEither) shouldEqual Right(List(editor))
    }

    "pass error from connector" in {
      val tournamentId = 123
      connector.tournamentPageResponses.put(tournamentId, EitherT.leftT(new RuntimeException("tournament page error")))
      provider.getEditors(tournamentId).pipe(awaitError) should have message "tournament page error"
    }
  }
}
