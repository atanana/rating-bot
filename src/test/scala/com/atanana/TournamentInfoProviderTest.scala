package com.atanana

import cats.data.EitherT
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.data.Editor
import com.atanana.parsers.TournamentPageParser
import com.atanana.providers.TournamentInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

class TournamentInfoProviderTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val connector = stub[Connector]
  private val parser = stub[TournamentPageParser]

  private val provider = new TournamentInfoProvider(connector, parser)

  "TournamentInfoProvider" should {

    "provide correct info" in {
      val tournamentId = 123
      val page = "tournament page"
      val editor = mock[Editor]
      (connector.getTournamentPage _).when(tournamentId).returns(EitherT.rightT[Future, Throwable](page))
      (parser.getEditors _).when(page).returns(List(editor))

      provider.getEditors(tournamentId).pipe(awaitEither) shouldEqual Right(List(editor))
    }

    "pass error from connector" in {
      val tournamentId = 123
      (connector.getTournamentPage _).when(tournamentId).returns(EitherT.leftT(new RuntimeException("tournament page error")))
      provider.getEditors(tournamentId).pipe(awaitError) should have message "tournament page error"
    }
  }
}
