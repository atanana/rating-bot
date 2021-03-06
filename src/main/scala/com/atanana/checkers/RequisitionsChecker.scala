package com.atanana.checkers

import java.time.{LocalDateTime, ZoneId}

import com.atanana.data.{Requisition, RequisitionData, RequisitionsCheckResult}

class RequisitionsChecker {
  def check(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): RequisitionsCheckResult = {
    RequisitionsCheckResult(
      getNewRequisitions(oldRequisitions, newRequisitions),
      getCancelledRequisitions(getNotFinishedRequisitions(oldRequisitions), newRequisitions)
    )
  }

  private def getNotFinishedRequisitions(oldRequisitions: Set[Requisition]) = {
    val finishTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusHours(1)
    oldRequisitions.filter(requisition => requisition.dateTime.isAfter(finishTime))
  }

  //noinspection FilterOtherContains
  def getNewRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): Set[RequisitionData] = {
    newRequisitions.filter(newRequisition => !oldRequisitions.contains(newRequisition))
  }

  def getCancelledRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[Requisition]): Set[Requisition] = {
    oldRequisitions -- newRequisitions
  }
}

object RequisitionsChecker {
  def apply(): RequisitionsChecker = new RequisitionsChecker()
}