package com.atanana.data

import java.time.LocalDateTime

case class Requisition(tournament: String, agent: String, dateTime: LocalDateTime)

case class RequisitionData(tournament: String, tournamentId: Int, agent: String, dateTime: LocalDateTime) {
  def toRequisition = Requisition(tournament, agent, dateTime)
}