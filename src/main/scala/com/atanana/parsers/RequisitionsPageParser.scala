package com.atanana.parsers

import scala.util.Try

trait RequisitionsPageParser {

  def additionalData(agent: String, html: String): Try[RequisitionAdditionalData]
}
