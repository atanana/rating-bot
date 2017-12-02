package com.atanana.data

import scala.util.Try

case class Data(tournaments: Set[Tournament], requisitions: Set[Requisition])

case class ParsedData(tournaments: Set[TournamentData], requisitions: Try[Set[RequisitionData]]) {
  def toData: Data = Data(tournaments, requisitions.getOrElse[Set[RequisitionData]](Set.empty))
}

object ParsedData {
  implicit def toData(data: ParsedData): Data = data.toData
}