package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.Editor

trait TournamentPageParser {

  def getEditors(html: String): List[Editor]
}
