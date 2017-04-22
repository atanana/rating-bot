package com.atanana

import java.time.LocalDateTime

import com.atanana.checkers.MainChecker
import com.atanana.data._
import com.atanana.providers.PollingDataProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

class ProcessorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var processor: Processor = _

  var provider: PollingDataProvider = _
  var store: JsonStore = _
  var checker: MainChecker = _
  var poster: Poster = _
  var checkResultsHandler: CheckResultHandler = _

  before {
    provider = stub[PollingDataProvider]
    store = mock[JsonStore]
    checker = stub[MainChecker]
    poster = mock[Poster]
    checkResultsHandler = mock[CheckResultHandler]

    processor = Processor(provider, store, checker, checkResultsHandler)
  }

  "Processor" should {
    "posts about changes" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      (store.write _).expects(*)
      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      (checker.check _).when(storedData, parsedData).returns(checkResult)
      (checkResultsHandler.processCheckResult _).expects(checkResult)

      processor.processCommand("poll")
    }

    "save new data" in {
      val parsedData = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      (store.write _).expects(parsedData.toData)
      (checker.check _).when(storedData, parsedData)
      (checkResultsHandler.processCheckResult _).expects(*)

      processor.processCommand("poll")
    }

    "not save data when no changes" in {
      val parsedData = setUpDefaults()
      val storedData = parsedData.toData
      (store.read _).expects().returns(storedData)
      (checker.check _).when(storedData, parsedData)
      (checkResultsHandler.processCheckResult _).expects(*)

      processor.processCommand("poll")
    }

    "should fail on unknown command" in {
      an[RuntimeException] should be thrownBy processor.processCommand("unknown")
    }
  }

  private def setUpDefaults() = {
    val parsedData = ParsedData(
      Set(TournamentData(1, "tournament 1", "link 1", 1f, 1, 1)),
      Set(RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now()))
    )
    (provider.data _).when().returns(parsedData)
    parsedData
  }
}
