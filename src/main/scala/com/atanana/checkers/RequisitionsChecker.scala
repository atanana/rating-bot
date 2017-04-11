package com.atanana.checkers

import java.time.{LocalDateTime, ZoneId}

import com.atanana.data.Requisition

class RequisitionsChecker {
  def check(oldRequisitions: Set[Requisition], newRequisitions: Set[Requisition]): RequisitionsCheckResult = {
    val notFinishedRequisitions = getNotFinishedRequisitions(oldRequisitions)
    RequisitionsCheckResult(
      getNewRequisitions(notFinishedRequisitions, newRequisitions),
      getCancelledRequisitions(notFinishedRequisitions, newRequisitions)
    )
  }

  private def getNotFinishedRequisitions(oldRequisitions: Set[Requisition]) = {
    val finishTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusHours(1)
    oldRequisitions.filter(requisition => requisition.dateTime.isAfter(finishTime))
  }

  def getNewRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[Requisition]): Set[Requisition] = {
    newRequisitions -- oldRequisitions
  }

  def getCancelledRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[Requisition]): Set[Requisition] = {
    oldRequisitions -- newRequisitions
  }
}

object RequisitionsChecker {
  def apply(): RequisitionsChecker = new RequisitionsChecker()
}

case class RequisitionsCheckResult(newRequisitions: Set[Requisition], cancelledRequisitions: Set[Requisition])