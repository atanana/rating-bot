package com.atanana

import java.time.{DayOfWeek, LocalDate}

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

  def composeChangedResult(data: TournamentData, oldResult: Int): String = {
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
}

object MessageComposer {
  def apply(): MessageComposer = new MessageComposer()
}