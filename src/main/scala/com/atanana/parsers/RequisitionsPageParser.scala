package com.atanana.parsers

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

import scala.util.Try

class RequisitionsPageParser {
  def teamsCount(agent: String, html: String): Try[Int] = {
    Try {
      val document = JsoupBrowser().parseString(html)
      val requisitions = document >> elementList("table.colored_table tbody tr")
      requisitions
        .map(tryParseRequisition)
        .flatMap(_.toOption.toList)
        .find(_.agent == agent)
        .map(_.teamsCount)
        .getOrElse(throw new RuntimeException("No such agent on requisition page!"))
    }
  }

  private def tryParseRequisition(row: Element): Try[RequisitionRowData] = {
    Try {
      val data = row.children.toList
      val teamsData = data.last.text
      val teamsCount = teamsData.substring(teamsData.lastIndexOf('/') + 1).trim.toInt
      RequisitionRowData(
        data(4).text.trim,
        teamsCount
      )
    }
  }
}

private case class RequisitionRowData(agent: String, teamsCount: Int)

object RequisitionsPageParser {
  def apply(): RequisitionsPageParser = new RequisitionsPageParser()
}