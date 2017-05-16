package com.atanana

import java.time.LocalDateTime

import com.atanana.data.{ChangedTournament, Editor, Requisition, TournamentData}
import org.scalatest.{Matchers, WordSpecLike}

class MessageComposerTest extends WordSpecLike with Matchers {
  "MessageComposer" should {
    "valid message new result 1" in {
      MessageComposer().composeNewResult(TournamentData(123, "test name", "test link", 123, -33, 0)) shouldEqual
        "Воздрочим же! На турнире test name нас слегка поимели. По итогам команда заняла 123.0 место и получила -33 рейта. \ntest link"
    }
  }

  "valid message new result 2" in {
    MessageComposer().composeNewResult(TournamentData(123, "test name", "test link", 123, 0, 0)) shouldEqual
      "Воздрочим же! На турнире test name мы сыграли ровно. По итогам команда заняла 123.0 место и получила 0 рейта. \ntest link"
  }

  "valid message new result 3" in {
    MessageComposer().composeNewResult(TournamentData(123, "test name", "test link", 123, 15, 0)) shouldEqual
      "Воздрочим же! На турнире test name нам немного повезло. По итогам команда заняла 123.0 место и получила 15 рейта. \ntest link"
  }

  "valid message new result 4" in {
    MessageComposer().composeNewResult(TournamentData(123, "test name", "test link", 123, 120, 0)) shouldEqual
      "Воздрочим же! На турнире test name мы видимо кому-то заплатили. По итогам команда заняла 123.0 место и получила 120 рейта. \ntest link"
  }

  "valid changed result" in {
    MessageComposer().composeChangedResult(ChangedTournament(TournamentData(123, "test name", "test link", 123, 15, 20), 10)) shouldEqual
      s"Сегодня ${MessageComposer().currentDay()}, а значит настало время дрочить на рейтинг! На турнире test name у нас было 10, а стало 20 взятых. Новый результат: 123.0 место и 15 рейтига. \ntest link"
  }

  "valid new requisition" in {
    val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.of(2017, 4, 11, 18, 45))
    val editors = List(Editor("editor 1"), Editor("editor 2"))
    MessageComposer().composeNewRequisition(requisition, editors) shouldEqual
      "А в следующий раз нас поимеют на турнире под названием tournament 1 который состоится 11 апреля 2017 18:45:00. Ответственный: agent 1.\nРедакторы: editor 1, editor 2"
  }

  "valid cancelled requisition" in {
    MessageComposer().composeCancelledRequisition(Requisition("tournament 1", "agent 1", LocalDateTime.of(2017, 4, 11, 18, 45))) should
      endWith("! agent 1 вёл себя подозрительно и посему tournament 1 в 11 апреля 2017 18:45:00 отменяется!")
  }

  "valid requisition reminder" in {
    val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.of(2017, 4, 11, 18, 45))
    MessageComposer().composeRequisitionReminder(requisition) shouldEqual
      "Напоминаю, что завтра состоится очередная рейтинг-оргия под названием tournament 1. Командовать парадом будет agent 1"
  }
}
