package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.TestUtils.{awaitEither, awaitError}
import com.atanana.ratingbot.data.{Editor, TournamentInfo}
import com.atanana.ratingbot.mocks.{MockTournamentInfoParser, MockTournamentPageParser}
import com.atanana.ratingbot.net.MockConnector
import com.atanana.ratingbot.parsers.TournamentPageParserImpl
import com.atanana.ratingbot.providers.TournamentInfoProviderImpl
import com.atanana.ratingbot.types.Ids.TournamentId
import com.atanana.ratingbot.types.Pages.TournamentInfoPage
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

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
      connector.tournamentPageResponses.put(tournamentId, EitherT.rightT(page))
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
