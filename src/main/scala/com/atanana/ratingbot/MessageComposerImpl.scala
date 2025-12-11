package com.atanana.ratingbot

import com.atanana.ratingbot.MessageComposer
import com.atanana.ratingbot.MessageComposerImpl.{alarms, timePattern}
import com.atanana.ratingbot.data.*
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.net.UriComposer

import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.util.Locale
import scala.language.implicitConversions
import scala.util.Random

class MessageComposerImpl(config: Config) extends MessageComposer {

  override def composeNewResult(data: TournamentResult, info: TournamentInfo): String = {
    val link = UriComposer.tournamentPageUri(data.id)
    s"Воздрочим же! На турнире [${info.name}]($link) ${scoreDescription(data.bonus)}. По итогам команда заняла *${printPlace(data.position)}* " +
      s"место и получила *${data.bonus}* рейта."
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

  private def printPlace(place: Float): String = {
    if place % 1 == 0 then {
      place.toInt.toString
    } else {
      place.toString
    }
  }

  override def composeNewRequisition(requisition: Requisition, editors: List[Editor]): String = {
    s"А в следующий раз нас поимеют на турнире под названием *${requisition.tournament}* который " +
      s"состоится *${requisition.dateTime.format(timePattern)}*. Ответственный: ${requisition.agent}." +
      s"\nРедакторы: ${getEditorsString(editors)}\n*${requisition.questionsCount} вопросов*"
  }

  private def getEditorsString(editors: List[Editor]) = {
    editors.map(_.data).mkString(", ")
  }

  override def composeRequisitionReminder(requisition: Requisition): String = {
    s"Напоминаю, что завтра состоится очередная рейтинг-оргия под названием *${requisition.tournament} (${requisition.questionsCount} вопросов)*. " +
      s"Командовать парадом будет ${requisition.agent}"
  }

  override def composeCancelledRequisition(requisition: Requisition): String = {
    randomAlarm + s"! ${requisition.agent} вёл себя подозрительно и посему *${requisition.tournament}* в " +
      s"*${requisition.dateTime.format(timePattern)}* отменяется!"
  }

  private def randomAlarm: String = {
    alarms(Random.nextInt(alarms.size))
  }

  override def composeTeamPositionsMessage(info: TeamPositionsInfo): String = {
    s"""
       |Небольшая сводка по новому релизу:
       |
       |🏆 текущий рейтинг — *${info.currentRating}*
       |🏆 место по городу — *${info.cityPosition}*
       |🏆 место по стране — *${info.countryPosition}*
       |🏆 место в общем рейтинге — *${info.currentPosition}*
       |🏆 до топ-${config.topCommandsThreshold} осталось — *${info.topRatingDifference}*
       |🏆 ${targetCountryTeamMessage(info.targetCountryRatingTeam)}
       |🏆 *${-info.overtakingCountryRatingTeam.ratingDifference}* осталось команде *${printTeam(info.overtakingCountryRatingTeam)}* чтобы догнать нас по стране
       |
       |${targetAllTeamMessage(info.targetAllRatingTeam)}
    """.stripMargin
  }

  private def targetCountryTeamMessage(teamOption: Option[TargetTeam]): String =
    teamOption.map(team => s"*${team.ratingDifference}* осталось до следующей команды по стране — *${printTeam(team)}*")
      .getOrElse("мы первая команда в стране! Нужно постараться не обосраться на следующей неделе")

  private def targetAllTeamMessage(teamOption: Option[TargetTeam]): String =
    teamOption.map(team => s"За эту неделю было бы неплохо обойти хотя бы команду ${printTeam(team)}, до которой осталось ${team.ratingDifference} очков")
      .getOrElse("мы первая команда в стране! Охуеть просто! Нужно постараться не обосраться на следующей неделе")

  private def printTeam(team: TargetTeam): String = s"${team.name} (${team.city})"

  override def composeChangedResult(changedTournament: ChangedTournament, info: TournamentInfo): String = {
    val data = changedTournament.tournament
    val oldResult = changedTournament.oldScore
    val link = UriComposer.tournamentPageUri(data.id)
    s"Сегодня ${currentDay()}, а значит настало время дрочить на рейтинг! На турнире ${info.name} у нас было $oldResult, " +
      s"а стало ${data.questionsCount} взятых. Новый результат: ${printPlace(data.position)} место и ${data.bonus} рейтига. \n$link"
  }

  override def currentDay(): String = LocalDate.now().getDayOfWeek match {
    case DayOfWeek.MONDAY => "понедельник"
    case DayOfWeek.TUESDAY => "вторник"
    case DayOfWeek.WEDNESDAY => "среда"
    case DayOfWeek.THURSDAY => "четверг"
    case DayOfWeek.FRIDAY => "пятница"
    case DayOfWeek.SATURDAY => "суббота"
    case DayOfWeek.SUNDAY => "воскресенье"
  }
}

object MessageComposerImpl {
  val alarms: Seq[String] = List("Ахтунг", "Алярм", "Бида-бида", "Усё пропало")

  val timePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", new Locale("ru"))
}
