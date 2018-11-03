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
      val isRealTeam = isReal(data(7))
      Team(
        id = parseId(data(6)),
        name = parseName(data(7), isRealTeam),
        city = data(8).text,
        rating = data(3).text.toInt,
        position = data(1).text.replace(',', '.').toFloat,
        isReal = isRealTeam
      )
    }
  }

  private def parseId(idElement: Element): Int = idElement.text.filter(_.toInt != 160).toInt

  private def parseName(nameElement: Element, isReal: Boolean): String = {
    val name = nameElement.text
    if (!isReal && name.endsWith(" #")) {
      name.dropRight(2)
    } else {
      name
    }
  }

  private def isReal(nameElement: Element): Boolean = (nameElement >?> element(".non_dbs_team")).isEmpty
}
