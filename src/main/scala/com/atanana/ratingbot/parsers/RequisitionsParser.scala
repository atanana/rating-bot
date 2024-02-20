package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.PartialRequisitionData

import scala.util.Try

trait RequisitionsParser {

  def getRequisitionsData(html: String): Try[List[PartialRequisitionData]]
}
