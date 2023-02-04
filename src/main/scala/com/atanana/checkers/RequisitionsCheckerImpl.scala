package com.atanana.checkers

import com.atanana.data.{Requisition, RequisitionData, RequisitionsCheckResult}

import java.time.{LocalDateTime, ZoneId}

class RequisitionsCheckerImpl extends RequisitionsChecker {
  override def check(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): RequisitionsCheckResult = {
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
  override def getNewRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): Set[RequisitionData] = {
    newRequisitions.filter(newRequisition => !oldRequisitions.contains(newRequisition))
  }

  private def getCancelledRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[Requisition]): Set[Requisition] = {
    oldRequisitions -- newRequisitions
  }
}

object RequisitionsCheckerImpl {
  def apply(): RequisitionsCheckerImpl = new RequisitionsCheckerImpl()
}