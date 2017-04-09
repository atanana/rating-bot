package com.atanana

import com.atanana.data.{Data, Tournament, TournamentData}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FunSuite}

class DataCheckerTest extends FunSuite with BeforeAndAfter with MockFactory {
  private val MESSAGE_NEW = "test message"
  private val MESSAGE_CHANGED = "test message 2"

  var poster: Poster = _
  var store: JsonStore = _
  var messageComposer: MessageComposer = _

  var dataChecker: DataChecker = _

  before {
    poster = mock[Poster]
    store = mock[JsonStore]
    messageComposer = mock[MessageComposer]

    dataChecker = DataChecker(poster, store, messageComposer)
  }

  test("no new tournaments") {
    (store.read _).expects().returns(Data(Set(Tournament(1, 0), Tournament(2, 0), Tournament(3, 0)), Set.empty))

    dataChecker.check(List(
      TournamentData(3, "test 3", "link 3", 0f, 0, 0),
      TournamentData(2, "test 2", "link 2", 0f, 0, 0),
      TournamentData(1, "test 1", "link 1", 0f, 0, 0)
    ))
  }

  test("new tournaments") {
    (store.read _).expects().returns(Data(Set(Tournament(1, 0)), Set.empty))
    val tournamentData3: TournamentData = TournamentData(3, "test 3", "link 3", 3.3f, 3, 0)
    val tournamentData2: TournamentData = TournamentData(2, "test 2", "link 2", 2.2f, 2, 0)

    (poster.post _).expects(MESSAGE_NEW).repeat(2)

    (messageComposer.composeNewResult _).expects(tournamentData3).returns(MESSAGE_NEW)
    (messageComposer.composeNewResult _).expects(tournamentData2).returns(MESSAGE_NEW)

    (store.write _).expects(Data(Set(Tournament(1, 0), Tournament(2, 0), Tournament(3, 0)), Set.empty))

    dataChecker.check(List(
      tournamentData3,
      tournamentData2,
      TournamentData(1, "test 1", "link 1", 1.1f, 1, 0)
    ))
  }

  test("not post first time") {
    (store.read _).expects().returns(Data(Set.empty, Set.empty))

    (store.write _).expects(Data(Set(Tournament(1, 0), Tournament(2, 1), Tournament(3, 0)), Set.empty))

    dataChecker.check(List(
      TournamentData(3, "test 3", "link 3", 3.3f, 3, 0),
      TournamentData(2, "test 2", "link 2", 2.2f, 2, 1),
      TournamentData(1, "test 1", "link 1", 1.1f, 1, 0)
    ))
  }

  test("questions changed") {
    (store.read _).expects().returns(Data(Set(Tournament(1, 0), Tournament(2, 0), Tournament(3, 0)), Set.empty))
    val tournamentData2: TournamentData = TournamentData(2, "test 2", "link 2", 2.2f, 2, 1)

    (poster.post _).expects(MESSAGE_CHANGED)

    (messageComposer.composeChangedResult _).expects(tournamentData2, 0).returns(MESSAGE_CHANGED)

    (store.write _).expects(Data(Set(Tournament(1, 0), Tournament(2, 1), Tournament(3, 0)), Set.empty))

    dataChecker.check(List(
      TournamentData(3, "test 3", "link 3", 3.3f, 3, 0),
      tournamentData2,
      TournamentData(1, "test 1", "link 1", 1.1f, 1, 0)
    ))
  }
}
