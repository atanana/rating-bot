package com.atanana.data

import scala.language.implicitConversions

case class Data(tournaments: Set[Tournament], requisitions: Set[Requisition])

case class ParsedData(tournaments: Set[TournamentData], requisitions: Set[RequisitionData]) {
  def toData: Data = Data(tournaments, requisitions)
}

object ParsedData {
  implicit def toData(data: ParsedData): Data = data.toData
}