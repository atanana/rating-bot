package com.atanana.parsers

import com.atanana.data.Team
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.util.Try

class TeamsPageParser {
  def getTeams(html: String): List[Team] = {
    val document = JsoupBrowser().parseString(html)
    val teamRows = document >> elementList("table.teams_table tbody tr")
    teamRows.map(tryParseTeam).flatMap(_.toOption.toList)
  }

  private def tryParseTeam(row: Element) = {
    Try {
      val data = row.children.toList
      Team(
        data(6).text.filter(_.toInt != 160).toInt,
        data(7).text,
        data(8).text,
        data(3).text.toInt,
        data(1).text.replace(',', '.').toFloat
      )
    }
  }
}
