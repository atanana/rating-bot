package com.atanana

import java.time.LocalDateTime

import com.atanana.checkers.MainChecker
import com.atanana.data._
import com.atanana.parsers.{CsvParser, RequisitionsParser}
import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, WordSpecLike}

class ProcessorTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var processor: Processor = _

  var connector: Connector = _
  var csvParser: CsvParser = _
  var requisitionsParser: RequisitionsParser = _
  var store: JsonStore = _
  var checker: MainChecker = _
  var poster: Poster = _
  var checkResultsHandler: CheckResultHandler = _

  before {
    connector = stub[Connector]
    csvParser = stub[CsvParser]
    requisitionsParser = stub[RequisitionsParser]
    store = mock[JsonStore]
    checker = stub[MainChecker]
    poster = mock[Poster]
    checkResultsHandler = mock[CheckResultHandler]

    processor = Processor(connector, csvParser, requisitionsParser, store, checker, checkResultsHandler)
  }

  "Processor" should {
    "posts about changes" in {
      val (tournament: TournamentData, requisition: RequisitionData) = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      (store.write _).expects(*)
      val checkResult = CheckResult(TournamentsCheckResult(Set.empty, Set.empty), RequisitionsCheckResult(Set.empty, Set.empty))
      (checker.check _).when(storedData, Set(tournament), Set(requisition)).returns(checkResult)
      (checkResultsHandler.processCheckResult _).expects(checkResult)

      processor.process()
    }

    "save new data" in {
      val (tournament: TournamentData, requisition: RequisitionData) = setUpDefaults()
      val storedData = Data(Set.empty, Set.empty)
      (store.read _).expects().returns(storedData)
      (store.write _).expects(Data(Set(tournament.toTournament), Set(requisition.toRequisition)))
      (checker.check _).when(storedData, Set(tournament), Set(requisition))
      (checkResultsHandler.processCheckResult _).expects(*)

      processor.process()
    }

    "not save data when no changes" in {
      val (tournament: TournamentData, requisition: RequisitionData) = setUpDefaults()
      val storedData = Data(Set(tournament.toTournament), Set(requisition.toRequisition))
      (store.read _).expects().returns(storedData)
      (checker.check _).when(storedData, Set(tournament), Set(requisition))
      (checkResultsHandler.processCheckResult _).expects(*)

      processor.process()
    }
  }

  private def setUpDefaults() = {
    (connector.getTeamPage _).when().returns("team page")
    (connector.getRequisitionPage _).when().returns("requisitions page")
    val tournament = TournamentData(1, "tournament 1", "link 1", 1f, 1, 1)
    (csvParser.getTournamentsData _).when("team page").returns(List(tournament))
    val requisition = RequisitionData("tournament 1", 1, "agent 1", LocalDateTime.now())
    (requisitionsParser.getRequisitionsData _).when("requisitions page").returns(List(requisition))
    (tournament, requisition)
  }
}
