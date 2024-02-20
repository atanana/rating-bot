package com.atanana.ratingbot.parsers

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

import scala.util.Try

class RequisitionsPageParserImpl extends RequisitionsPageParser {

  override def additionalData(agent: String, html: String): Try[RequisitionAdditionalData] = {
    Try {
      val document = JsoupBrowser().parseString(html)
      val requisitions = document >> elementList("table.colored_table tbody tr")
      requisitions
        .map(tryParseRequisition)
        .flatMap(_.toOption.toList)
        .find(_.agent == agent)
        .map(_.toAdditionalData)
        .getOrElse(throw new RuntimeException("No such agent on requisition page!"))
    }
  }

  private def tryParseRequisition(row: Element): Try[RequisitionRowData] = {
    Try {
      val data = row.children.toList
      val teamsData = data.last.text
      val teamsCount = teamsData.substring(teamsData.lastIndexOf('/') + 1).trim.toInt
      RequisitionRowData(
        data(3).text.trim,
        data(4).text.trim,
        teamsCount
      )
    }
  }
}

private case class RequisitionRowData(venue: String, agent: String, teamsCount: Int) {
  def toAdditionalData: RequisitionAdditionalData = RequisitionAdditionalData(venue, teamsCount)
}

case class RequisitionAdditionalData(venue: String, teamsCount: Int)
