package com.atanana.ratingbot

import cats.implicits.*
import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.MessageComposerImpl
import com.atanana.ratingbot.TestUtils.fakeConfig
import com.atanana.ratingbot.data.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class MessageComposerTest extends AnyWordSpecLike with Matchers {
  "MessageComposer" should {
    val messageComposer = MessageComposerImpl(fakeConfig)

    "valid message new result 1" in {
      messageComposer.composeNewResult(TournamentResult(123, 0, 123, -33), TournamentInfo("test name", 36)) shouldEqual
        "Воздрочим же! На турнире [test name](https://rating.chgk.info/tournament/123) нас слегка поимели. По итогам команда заняла *123* место и получила *-33* рейта."
    }

    "valid message new result 2" in {
      messageComposer.composeNewResult(TournamentResult(123, 0, 123, 0), TournamentInfo("test name", 36)) shouldEqual
        "Воздрочим же! На турнире [test name](https://rating.chgk.info/tournament/123) мы сыграли ровно. По итогам команда заняла *123* место и получила *0* рейта."
    }

    "valid message new result 3" in {
      messageComposer.composeNewResult(TournamentResult(123, 0, 123, 15), TournamentInfo("test name", 36)) shouldEqual
        "Воздрочим же! На турнире [test name](https://rating.chgk.info/tournament/123) нам немного повезло. По итогам команда заняла *123* место и получила *15* рейта."
    }

    "valid message new result 4" in {
      messageComposer.composeNewResult(TournamentResult(123, 0, 123.5f, 120), TournamentInfo("test name", 36)) shouldEqual
        "Воздрочим же! На турнире [test name](https://rating.chgk.info/tournament/123) мы видимо кому-то заплатили. По итогам команда заняла *123.5* место и получила *120* рейта."
    }

    "valid changed result" in {
      messageComposer.composeChangedResult(ChangedTournament(TournamentResult(123, 20, 123, 15), 10), TournamentInfo("test name", 36)) shouldEqual
        s"Сегодня ${messageComposer.currentDay()}, а значит настало время дрочить на рейтинг! На турнире test name у нас было 10, а стало 20 взятых. Новый результат: 123 место и 15 рейтига. \nhttps://rating.chgk.info/tournament/123"
    }

    "valid new requisition" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.of(2017, 4, 11, 18, 45), 36)
      val editors = List(Editor("editor 1"), Editor("editor 2"))
      messageComposer.composeNewRequisition(requisition, editors) shouldEqual
        "А в следующий раз нас поимеют на турнире под названием *tournament 1* который состоится *11 апреля 2017 18:45:00*. Ответственный: agent 1.\nРедакторы: editor 1, editor 2\n*36 вопросов*"
    }

    "valid cancelled requisition" in {
      messageComposer.composeCancelledRequisition(Requisition("tournament 1", "agent 1", LocalDateTime.of(2017, 4, 11, 18, 45))) should
        endWith("! agent 1 вёл себя подозрительно и посему *tournament 1* в *11 апреля 2017 18:45:00* отменяется!")
    }

    "valid requisition reminder" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.of(2017, 4, 11, 18, 45), 36)
      messageComposer.composeRequisitionReminder(requisition) shouldEqual
        "Напоминаю, что завтра состоится очередная рейтинг-оргия под названием *tournament 1 (36 вопросов)*. Командовать парадом будет agent 1"
    }

    "valid team positions reminder" in {
      val targetTeam = TargetTeam("target team", "target city", 20)
      val targetCountryTeam = TargetTeam("country team", "country city", 10)
      val overtakingTeam = TargetTeam("overtaking team", "overtaking city", -10)
      val info = TeamPositionsInfo(targetTeam.some, targetCountryTeam.some, overtakingTeam, 123, 200, 100, 20, 30)
      messageComposer.composeTeamPositionsMessage(info) shouldEqual
        s"""
           |Небольшая сводка по новому релизу:
           |
           |🏆 текущий рейтинг — *100*
           |🏆 место по городу — *20*
           |🏆 место по стране — *30*
           |🏆 место в общем рейтинге — *200*
           |🏆 до топ-100 осталось — *123*
           |🏆 *10* осталось до следующей команды по стране — *country team (country city)*
           |🏆 *10* осталось команде *overtaking team (overtaking city)* чтобы догнать нас по стране
           |
           |За эту неделю было бы неплохо обойти хотя бы команду target team (target city), до которой осталось 20 очков
    """.stripMargin
    }

    "valid team positions reminder when team is on the first place" in {
      val overtakingTeam = TargetTeam("overtaking team", "overtaking city", -10)
      val info = TeamPositionsInfo(None, None, overtakingTeam, 123, 200, 100, 20, 30)
      messageComposer.composeTeamPositionsMessage(info) shouldEqual
        s"""
           |Небольшая сводка по новому релизу:
           |
           |🏆 текущий рейтинг — *100*
           |🏆 место по городу — *20*
           |🏆 место по стране — *30*
           |🏆 место в общем рейтинге — *200*
           |🏆 до топ-100 осталось — *123*
           |🏆 мы первая команда в стране! Нужно постараться не обосраться на следующей неделе
           |🏆 *10* осталось команде *overtaking team (overtaking city)* чтобы догнать нас по стране
           |
           |мы первая команда в стране! Охуеть просто! Нужно постараться не обосраться на следующей неделе
    """.stripMargin
    }
  }
}
