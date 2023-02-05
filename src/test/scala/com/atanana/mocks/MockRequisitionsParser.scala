package com.atanana.mocks

import com.atanana.data.PartialRequisitionData
import com.atanana.parsers.RequisitionsParser

import scala.collection.mutable
import scala.util.Try

class MockRequisitionsParser extends RequisitionsParser {

  val requisitions: mutable.Map[String, Try[List[PartialRequisitionData]]] = mutable.Map()

  override def getRequisitionsData(html: String): Try[List[PartialRequisitionData]] = requisitions(html)
}
