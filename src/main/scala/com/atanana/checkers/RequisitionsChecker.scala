package com.atanana.checkers

import java.time.{LocalDateTime, ZoneId}

import com.atanana.data.{Requisition, RequisitionsCheckResult}

class RequisitionsChecker {
  def check(oldRequisitions: Set[Requisition], newRequisitions: Set[Requisition]): RequisitionsCheckResult = {
    RequisitionsCheckResult(
      getNewRequisitions(oldRequisitions, newRequisitions),
      getCancelledRequisitions(getNotFinishedRequisitions(oldRequisitions), newRequisitions)
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