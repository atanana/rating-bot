package com.atanana

import com.atanana.data.TournamentData
import org.scalatest.FunSuite

class MessageComposerTest extends FunSuite {
  private val messageComposer: MessageComposer = MessageComposer()

  test("valid message new result 1") {
    assertResult("Воздрочим же! На турнире test name нас слегка поимели. По итогам команда заняла 123.0 место и получила -33 рейта. \ntest link") {
      messageComposer.composeNewResult(TournamentData(123, "test name", "test link", 123, -33, 0))
    }
  }

  test("valid message new result 2") {
    assertResult("Воздрочим же! На турнире test name мы сыграли ровно. По итогам команда заняла 123.0 место и получила 0 рейта. \ntest link") {
      messageComposer.composeNewResult(TournamentData(123, "test name", "test link", 123, 0, 0))
    }
  }

  test("valid message new result 3") {
    assertResult("Воздрочим же! На турнире test name нам немного повезло. По итогам команда заняла 123.0 место и получила 15 рейта. \ntest link") {
      messageComposer.composeNewResult(TournamentData(123, "test name", "test link", 123, 15, 0))
    }
  }

  test("valid message new result 4") {
    assertResult("Воздрочим же! На турнире test name мы видимо кому-то заплатили. По итогам команда заняла 123.0 место и получила 120 рейта. \ntest link") {
      messageComposer.composeNewResult(TournamentData(123, "test name", "test link", 123, 120, 0))
    }
  }

  test("valid changed result") {
    assertResult(s"Сегодня ${messageComposer.currentDay()}, а значит настало время дрочить на рейтинг! На турнире test name у нас было 10, а стало 20 взятых. Новый результат: 123.0 место и 15 рейтига. \ntest link") {
      messageComposer.composeChangedResult(TournamentData(123, "test name", "test link", 123, 15, 20), 10)
    }
  }
}
