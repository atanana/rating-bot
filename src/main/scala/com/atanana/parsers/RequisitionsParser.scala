package com.atanana.parsers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import com.atanana.data.Requisition
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.util.Try

class RequisitionsParser {
  def getRequisitionsData(html: String): List[Requisition] = {
    val document = JsoupBrowser().parseString(html)
    val requisitions = document >> elementList(".upcoming_synch tbody tr")
    requisitions
      .map(tryParseRequisitionRow)
      .flatMap(_.toOption.toList)
  }

  private def tryParseRequisitionRow(row: Element) = {
    Try({
      val data = row.children.toList
      Requisition(
        tournament = data.head.text,
        agent = data(2).text,
        dateTime = LocalDateTime.parse(data(3).text, RequisitionsParser.timePattern)
      )
    })
  }
}

object RequisitionsParser {
  val timePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", new Locale("ru"))

  def apply(): RequisitionsParser = new RequisitionsParser()
}