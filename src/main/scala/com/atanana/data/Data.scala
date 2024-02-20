package com.atanana.data

import scala.language.implicitConversions

case class Data(tournaments: Set[Tournament], requisitions: Set[Requisition])

case class ParsedData(tournaments: Set[TournamentResult], requisitions: Set[RequisitionData]) {
  def toData: Data = Data(tournaments.map(result => Tournament(result.id, result.questionsCount)), requisitions)
}

object ParsedData {
  implicit def toData(data: ParsedData): Data = data.toData
}