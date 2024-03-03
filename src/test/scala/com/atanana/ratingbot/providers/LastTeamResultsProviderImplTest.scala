package com.atanana.ratingbot.providers

import cats.data.EitherT
import com.atanana.ratingbot.Conversions.{fromIntToTeamId, fromIntToTournamentId, fromStringToTeamTournamentsPage, fromStringToTournamentResultsPage}
import com.atanana.ratingbot.TestUtils.awaitEither
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.mocks.{MockTeamTournamentsParser, MockTournamentResultsParser}
import com.atanana.ratingbot.net.MockConnector
import com.atanana.ratingbot.providers.LastTeamResultsProviderImpl
import com.atanana.ratingbot.types.Ids.TournamentId
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.chaining.scalaUtilChainingOps

class LastTeamResultsProviderImplTest extends AnyWordSpecLike with Matchers {

  private val connector = MockConnector()
  private val teamTournamentsParser = MockTeamTournamentsParser()
  private val tournamentResultsParser = MockTournamentResultsParser()
  private val provider = new LastTeamResultsProviderImpl(connector, teamTournamentsParser, tournamentResultsParser)

  "LastTeamResultsProvider" should {

    "return last tournaments results" in {
      connector.teamTournamentsPage = EitherT.rightT("tournaments page")
      teamTournamentsParser.results("tournaments page") = Success((1 to 50).map(TournamentId(_)).toSet)
      for i <- 20 to 50 do {
        connector.tournamentResultsPage(i) = EitherT.rightT(s"tournament results $i")
        tournamentResultsParser.results((s"tournament results $i", i, 123)) = Success(Some(TournamentResult(i, i, i, i)))
      }

      val results = (50 to 21 by -1).map(i => TournamentResult(i, i, i, i)).toSet
      provider.getLastTeamResults(123).pipe(awaitEither) shouldEqual Right(results)
    }

    "filter results without rating" in {
      connector.teamTournamentsPage = EitherT.rightT("tournaments page")
      teamTournamentsParser.results("tournaments page") = Success((1 to 50).map(TournamentId(_)).toSet)
      for i <- 20 to 50 do {
        connector.tournamentResultsPage(i) = EitherT.rightT(s"tournament results $i")
        tournamentResultsParser.results((s"tournament results $i", i, 123)) = if i % 2 == 0 then Success(Some(TournamentResult(i, i, i, i))) else Success(None)
      }

      val results = (50 to 21 by -1)
        .filter(_ % 2 == 0)
        .map(i => TournamentResult(i, i, i, i)).toSet
      provider.getLastTeamResults(123).pipe(awaitEither) shouldEqual Right(results)
    }
  }
}
