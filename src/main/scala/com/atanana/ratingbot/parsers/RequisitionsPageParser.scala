package com.atanana.ratingbot.parsers

import scala.util.Try

trait RequisitionsPageParser {

  def additionalData(agent: String, html: String): Try[RequisitionAdditionalData]
}
