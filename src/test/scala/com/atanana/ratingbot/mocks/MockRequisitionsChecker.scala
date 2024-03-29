package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.checkers.RequisitionsChecker
import com.atanana.ratingbot.data.{Requisition, RequisitionData, RequisitionsCheckResult}

import scala.collection.mutable

class MockRequisitionsChecker extends RequisitionsChecker {

  val checkResults: mutable.Map[(Set[Requisition], Set[Requisition]), RequisitionsCheckResult] = mutable.Map[(Predef.Set[Requisition], Predef.Set[Requisition]), RequisitionsCheckResult]()

  override def check(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): RequisitionsCheckResult =
    checkResults((oldRequisitions, newRequisitions))

  override def getNewRequisitions(oldRequisitions: Set[Requisition], newRequisitions: Set[RequisitionData]): Set[RequisitionData] = ???
}
