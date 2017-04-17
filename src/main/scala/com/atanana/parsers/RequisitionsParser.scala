package com.atanana.parsers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import com.atanana.data.RequisitionData
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.util.Try

class RequisitionsParser {
  def getRequisitionsData(html: String): List[RequisitionData] = {
    val document = JsoupBrowser().parseString(html)
    val requisitions = document >> elementList(".upcoming_synch tbody tr")
    requisitions
      .map(tryParseRequisitionRow)
      .flatMap(_.toOption.toList)
  }

  private def tryParseRequisitionRow(row: Element) = {
    Try({
      val data = row.children.toList
      val tournamentLink = data.head >> element("a")
      RequisitionData(
        tournament = tournamentLink.text,
        tournamentId = getTournamentIdFromLink(tournamentLink),
        agent = data(2).text,
        dateTime = LocalDateTime.parse(data(3).text, RequisitionsParser.timePattern)
      )
    })
  }

  private def getTournamentIdFromLink(tournamentLink: Element) = {
    tournamentLink.attr("href").split('/').last.toInt
  }
}

object RequisitionsParser {
  val timePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", new Locale("ru"))

  def apply(): RequisitionsParser = new RequisitionsParser()
}