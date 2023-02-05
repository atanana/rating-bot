package com.atanana.parsers

import com.atanana.data.Editor

trait TournamentPageParser {

  def getEditors(html: String): List[Editor]
}
