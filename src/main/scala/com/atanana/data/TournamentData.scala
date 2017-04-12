package com.atanana.data

case class TournamentData(id: Int, name: String, link: String, place: Float, bonus: Int, questions: Int) {
  def toTournament: Tournament = Tournament(id, questions)
}
