package com.atanana.processors

import com.atanana.CheckResultHandler
import com.atanana.TestUtils.{getResult, getResultErrorMessage}
import com.atanana.checkers.MainChecker
import com.atanana.data._
import com.atanana.json.JsonStore
import com.atanana.providers.PollingDataProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.util.{Failure, Success}

class PollProcessorTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val provider = stub[PollingDataProvider]
  private val store = mock[JsonStore]
  private val checker = stub[MainChecker]
  private val checkResultsHandler = mock[CheckResultHandler]
  private val processor = new PollProcessor(provider, store, checker, checkResultsHandler)

  "Processor" should {
    "posts about changes" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      (store.write _).expects(*)
      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      (checker.check _).when(storedData, parsedData).returns(checkResult)
      (checkResultsHandler.processCheckResult _).expects(checkResult) returns Right()

      getResult(processor) shouldEqual Right()
    }

    "save new data" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      (store.write _).expects(parsedData.toData)
      (checker.check _).when(storedData, parsedData)
      (checkResultsHandler.processCheckResult _).expects(*) returns Right()

      getResult(processor) shouldEqual Right()
    }

    "not save data when no changes" in {
      val parsedData = setUpDefaults()
      val storedData = parsedData.toData
      (store.read _).expects().returns(storedData)
      (checker.check _).when(storedData, parsedData)
      (checkResultsHandler.processCheckResult _).expects(*) returns Right()

      getResult(processor) shouldEqual Right()
    }

    "not save data when requisitions failed" in {
      var parsedData = setUpDefaults()
      val storedData = parsedData.toData
      parsedData = parsedData.copy(requisitions = Failure(new RuntimeException))
      (store.read _).expects().returns(storedData)
      (checker.check _).when(storedData, parsedData)
      (checkResultsHandler.processCheckResult _).expects(*) returns Right()

      getResult(processor) shouldEqual Right()
    }

    "no posts and saves when no data" in {
      (provider.data _).when().returns(Future.successful(Left("error")))
      getResultErrorMessage(processor) shouldEqual "error"
    }

    "no posts and saves when no data async" in {
      (provider.data _).when().returns(Future.failed(new RuntimeException("123")))
      the[RuntimeException] thrownBy getResult(processor) should have message "123"
    }

    "not save data when posting failed" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      (checker.check _).when(storedData, parsedData).returns(checkResult)
      (checkResultsHandler.processCheckResult _).expects(checkResult) returns Left("post error")

      getResultErrorMessage(processor) shouldEqual "post error"
    }
  }

  private def setUpDefaults() = {
    val parsedData = ParsedData(
      Set(TournamentData(1, "tournament 1", "link 1", 1f, 1, 1)),
      Success(Set(RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())))
    )
    (provider.data _).when().returns(Future.successful(Right(parsedData)))
    parsedData
  }
}
