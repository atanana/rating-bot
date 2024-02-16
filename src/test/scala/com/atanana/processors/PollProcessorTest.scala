package com.atanana.processors

import cats.data.EitherT
import com.atanana.CheckResultHandlerImpl
import com.atanana.TestUtils.{getResult, getResultErrorMessage}
import com.atanana.checkers.MainCheckerImpl
import com.atanana.data.*
import com.atanana.json.JsonStoreImpl
import com.atanana.mocks.{MockCheckResultHandler, MockJsonStore, MockMainChecker, MockPollingDataProvider}
import com.atanana.providers.PollingDataProviderImpl
import com.atanana.types.Ids.TournamentId
import com.atanana.Conversions.fromIntToTournamentId
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PollProcessorTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {
  private val provider = new MockPollingDataProvider()
  private val store = new MockJsonStore()
  private val checker = new MockMainChecker()
  private val checkResultsHandler = new MockCheckResultHandler()
  private val processor = new PollProcessorImpl(provider, store, checker, checkResultsHandler)

  after {
    store.reset()
  }

  "Processor" should {
    "posts about changes" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      store.data = storedData
      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      checker.results.put((storedData, parsedData), checkResult)
      checkResultsHandler.results.put(checkResult, EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
    }

    "save new data" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      store.data = storedData
      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      checker.results.put((storedData, parsedData), checkResult)
      checkResultsHandler.results.put(checkResult, EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
      store.savedData shouldEqual parsedData.toData
    }

    "add missing tournaments" in {
      val parsedData = setUpDefaults()
      val tournament = TournamentData(2, "tournament 2", "link 2", 1f, 1, 1)
      val storedData = Data(Set(tournament), Set.empty)
      store.data = storedData

      var data = parsedData.toData
      data = data.copy(tournaments = data.tournaments + tournament)

      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      checker.results.put((storedData, parsedData), checkResult)
      checkResultsHandler.results.put(checkResult, EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
      store.savedData shouldEqual data
    }

    "not save data when no changes" in {
      val parsedData = setUpDefaults()
      val storedData = parsedData.toData
      store.data = storedData

      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      checker.results.put((storedData, parsedData), checkResult)
      checkResultsHandler.results.put(checkResult, EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
      store.savedData shouldEqual null
    }

    "no posts and saves when no data" in {
      provider.result = EitherT.leftT(new RuntimeException("error"))
      getResultErrorMessage(processor) shouldEqual "error"
    }

    "not save data when posting failed" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      store.data = storedData

      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      checker.results.put((storedData, parsedData), checkResult)
      checkResultsHandler.results.put(checkResult, EitherT.leftT(new RuntimeException("post error")))

      getResultErrorMessage(processor) shouldEqual "post error"
    }
  }

  private def setUpDefaults() = {
    val parsedData = ParsedData(
      Set(TournamentData(1, "tournament 1", "link 1", 1f, 1, 1)),
      Set(RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now()))
    )
    provider.result = EitherT.rightT[Future, Throwable](parsedData)
    parsedData
  }
}
