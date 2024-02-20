package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.data.PartialRequisitionData
import com.atanana.ratingbot.parsers.RequisitionsParser

import scala.collection.mutable
import scala.util.Try

class MockRequisitionsParser extends RequisitionsParser {

  val requisitions: mutable.Map[String, Try[List[PartialRequisitionData]]] = mutable.Map()

  override def getRequisitionsData(html: String): Try[List[PartialRequisitionData]] = requisitions(html)
}
