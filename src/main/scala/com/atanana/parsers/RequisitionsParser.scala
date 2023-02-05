package com.atanana.parsers

import com.atanana.data.PartialRequisitionData

import scala.util.Try

trait RequisitionsParser {

  def getRequisitionsData(html: String): Try[List[PartialRequisitionData]]
}
