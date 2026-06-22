package com.atanana.ratingbot.data

import scala.language.implicitConversions

case class Data(tournaments: Set[Tournament])

case class ParsedData(tournaments: Set[TournamentResult]) {
  def toData: Data = Data(tournaments.map(result => Tournament(result.id, result.questionsCount)))
}

object ParsedData {
  implicit def toData(data: ParsedData): Data = data.toData
}