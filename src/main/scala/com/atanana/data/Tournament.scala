package com.atanana.data

import scala.language.implicitConversions

case class Tournament(id: Int, score: Int)

case class TournamentData(id: Int, name: String, link: String, place: Float, bonus: Int, questions: Int) {
  def toTournament: Tournament = Tournament(id, questions)
}

object TournamentData {
  implicit def toTournament(data: TournamentData): Tournament = data.toTournament

  implicit def toTournamentSet(data: Set[TournamentData]): Set[Tournament] = data.map(toTournament)
}