package com.atanana.data

case class Data(tournaments: Set[Tournament], requisitions: Set[Requisition])

case class ParsedData(tournaments: Set[TournamentData], requisitions: Set[RequisitionData]) {
  def toData: Data = {
    Data(
      tournaments.map(_.toTournament),
      requisitions.map(_.toRequisition)
    )
  }
}