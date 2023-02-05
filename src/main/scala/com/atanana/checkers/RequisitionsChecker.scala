package com.atanana.checkers

import com.atanana.data.{Requisition, RequisitionData, RequisitionsCheckResult}

trait RequisitionsChecker {

  def check(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): RequisitionsCheckResult

  //noinspection FilterOtherContains
  def getNewRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): Set[RequisitionData]
}
