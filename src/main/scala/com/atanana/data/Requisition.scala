package com.atanana.data

import java.time.LocalDateTime

case class Requisition(tournament: String, agent: String, dateTime: LocalDateTime, questionsCount: Int = 0)

case class PartialRequisitionData(tournament: String, tournamentId: Int, agent: String, dateTime: LocalDateTime) {
  def toRequisitionData(questionsCount: Int) = RequisitionData(tournament, tournamentId, agent, dateTime, questionsCount)
}

case class RequisitionData(tournament: String, tournamentId: Int, agent: String, dateTime: LocalDateTime, questionsCount: Int = 0) {
  def toRequisition = Requisition(tournament, agent, dateTime, questionsCount)
}

object RequisitionData {
  implicit def toRequisition(data: RequisitionData): Requisition = data.toRequisition

  implicit def toRequisitionSet(data: Set[RequisitionData]): Set[Requisition] = data.map(toRequisition)
}