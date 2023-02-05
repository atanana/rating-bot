package com.atanana.mocks

import com.atanana.parsers.{RequisitionAdditionalData, RequisitionsPageParser}

import scala.collection.mutable
import scala.util.Try

class MockRequisitionsPageParser extends RequisitionsPageParser {

  val data: mutable.Map[(String, String), Try[RequisitionAdditionalData]] = mutable.Map()

  override def additionalData(agent: String, html: String): Try[RequisitionAdditionalData] = data((agent, html))
}
