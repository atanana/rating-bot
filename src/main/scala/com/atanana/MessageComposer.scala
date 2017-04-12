package com.atanana

import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.util.Locale

import com.atanana.MessageComposer.{alarms, timePattern}
import com.atanana.data.{ChangedTournament, Requisition, TournamentData}

import scala.util.Random

class MessageComposer {
  def composeNewResult(data: TournamentData): String = {
    s"Воздрочим же! На турнире ${data.name} ${scoreDescription(data.bonus)}. По итогам команда заняла ${data.place} " +
      s"место и получила ${data.bonus} рейта. \n${data.link}"
  }

  private def scoreDescription(score: Int): String = score match {
    case _ if score < -100 => "мы соснули как никогда"
    case _ if score < -50 => "нас крепко поимели"
    case _ if score < -25 => "нас слегка поимели"
    case _ if score < 0 => "мы плоховато сыграли"
    case 0 => "мы сыграли ровно"
    case _ if score > 100 => "мы видимо кому-то заплатили"
    case _ if score > 50 => "мы сыграли хорошо"
    case _ if score > 25 => "мы неплохо сыграли"
    case _ if score > 0 => "нам немного повезло"
  }

  def composeChangedResult(changedTournament: ChangedTournament): String = {
    val data = changedTournament.tournament
    val oldResult = changedTournament.oldScore
    s"Сегодня ${currentDay()}, а значит настало время дрочить на рейтинг! На турнире ${data.name} у нас было $oldResult, " +
      s"а стало ${data.questions} взятых. Новый результат: ${data.place} место и ${data.bonus} рейтига. \n${data.link}"
  }

  def currentDay(): String = LocalDate.now().getDayOfWeek match {
    case DayOfWeek.MONDAY => "понедельник"
    case DayOfWeek.TUESDAY => "вторник"
    case DayOfWeek.WEDNESDAY => "среда"
    case DayOfWeek.THURSDAY => "четверг"
    case DayOfWeek.FRIDAY => "пятница"
    case DayOfWeek.SATURDAY => "суббота"
    case DayOfWeek.SUNDAY => "воскресенье"
  }

  def composeNewRequisition(requisition: Requisition): String = {
    s"А в следующий нас поимеют на турнире под названием ${requisition.tournament} " +
      s"состоится ${requisition.dateTime.format(timePattern)} с подачи ${requisition.agent}"
  }

  def composeCancelledRequisition(requisition: Requisition): String = {
    randomAlarm + s"! ${requisition.agent} вёл себя подозрительно и посему ${requisition.tournament} в " +
      s"${requisition.dateTime.format(timePattern)} отменяется!"
  }

  private def randomAlarm: String = {
    alarms(Random.nextInt(alarms.size))
  }
}

object MessageComposer {
  val alarms = List("Ахтунг", "Алярм", "Бида-бида", "Усё пропало")

  val timePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", new Locale("ru"))

  def apply(): MessageComposer = new MessageComposer()
}