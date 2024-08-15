package com.atanana.ratingbot.checkers

import com.atanana.ratingbot.data.{Requisition, RequisitionData, RequisitionsCheckResult}

trait RequisitionsChecker {

  def check(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): RequisitionsCheckResult

  def getNewRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): Set[RequisitionData]
}
