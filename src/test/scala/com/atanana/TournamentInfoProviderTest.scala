package com.atanana

import cats.data.EitherT
import com.atanana.TestUtils.{awaitEither, awaitError}
import com.atanana.data.{Editor, TournamentInfo}
import com.atanana.mocks.{MockTournamentInfoParser, MockTournamentPageParser}
import com.atanana.net.MockConnector
import com.atanana.parsers.TournamentPageParserImpl
import com.atanana.providers.TournamentInfoProviderImpl
import com.atanana.types.Ids.TournamentId
import com.atanana.Conversions.fromIntToTournamentId
import com.atanana.types.Pages.TournamentInfoPage
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success
import scala.util.chaining.scalaUtilChainingOps

class TournamentInfoProviderTest extends AnyWordSpecLike with Matchers {
  private val connector = new MockConnector()
  private val tournamentPageParser = new MockTournamentPageParser()
  private val tournamentInfoPageParser = new MockTournamentInfoParser()
  private val provider = new TournamentInfoProviderImpl(connector, tournamentPageParser, tournamentInfoPageParser)

  "TournamentInfoProvider" should {

    "provide correct info" in {
      val tournamentId = 123
      val page = "tournament page"
      val editor = Editor("test")
      connector.tournamentPageResponses.put(tournamentId, EitherT.rightT[Future, Throwable](page))
      tournamentPageParser.editors = List(editor)

      provider.getEditors(tournamentId).pipe(awaitEither) shouldEqual Right(List(editor))
    }

    "pass error from connector" in {
      val tournamentId = 123
      connector.tournamentPageResponses.put(tournamentId, EitherT.leftT(new RuntimeException("tournament page error")))
      provider.getEditors(tournamentId).pipe(awaitError) should have message "tournament page error"
    }

    "provide tournament info" in {
      connector.tournamentInfoResponses(123) = EitherT.rightT(TournamentInfoPage("info"))
      tournamentInfoPageParser.results(TournamentInfoPage("info")) = Success(TournamentInfo("test", 36))

      provider.getInfo(123).pipe(awaitEither) shouldEqual Right(TournamentInfo("test", 36))
    }
  }
}
