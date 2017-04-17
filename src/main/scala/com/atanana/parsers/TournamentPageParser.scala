package com.atanana.parsers

import com.atanana.data.Editor
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

class TournamentPageParser {
  def getEditors(html: String): List[Editor] = {
    val document = JsoupBrowser().parseString(html)
    val cards = document >> elementList("div.card")
    cards.filter(isEditorsCard).flatMap(extractEditors)
  }

  private def extractEditors(card: Element): List[Editor] = {
    val rows = card >> elementList("span.row")
    rows.map(row => Editor(row.text))
  }

  private def isEditorsCard(card: Element) = {
    val title = card >> element("span.title")
    title.text == "Редакторы"
  }
}

object TournamentPageParser {
  def apply(): TournamentPageParser = new TournamentPageParser()
}