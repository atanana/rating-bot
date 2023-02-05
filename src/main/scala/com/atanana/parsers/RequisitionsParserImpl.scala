package com.atanana.parsers

import com.atanana.data.PartialRequisitionData
import com.typesafe.scalalogging.Logger
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.model.Element

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import scala.util.{Failure, Try}

class RequisitionsParserImpl extends RequisitionsParser {

  import RequisitionsParserImpl.logger

  override def getRequisitionsData(html: String): Try[List[PartialRequisitionData]] = {
    Try {
      val document = JsoupBrowser().parseString(html)
      document >> element(".navbar") // just check that we have valid html
      val requisitions = document >> elementList(".upcoming_synch tbody tr")
      requisitions
        .map(tryParseRequisitionRow)
        .flatMap(_.toOption.toList)
    } recoverWith {
      case e: Throwable =>
        logger.error("Cannot parse requisitions!", e)
        Failure(e)
    }
  }

  private def tryParseRequisitionRow(row: Element) = {
    Try {
      val data = row.children.toList
      val tournamentLink = data.head >> element("a")
      PartialRequisitionData(
        tournament = tournamentLink.text,
        tournamentId = getTournamentIdFromLink(tournamentLink),
        agent = data(2).text,
        dateTime = LocalDateTime.parse(data(3).text, RequisitionsParserImpl.timePattern)
      )
    }
  }

  private def getTournamentIdFromLink(tournamentLink: Element) = {
    tournamentLink.attr("href").split('/').last.toInt
  }
}

object RequisitionsParserImpl {
  val timePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", new Locale("ru"))
  private val logger = Logger(classOf[RequisitionsParserImpl])
}