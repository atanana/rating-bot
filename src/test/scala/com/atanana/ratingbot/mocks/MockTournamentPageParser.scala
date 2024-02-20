package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.data.Editor
import com.atanana.ratingbot.parsers.TournamentPageParser

class MockTournamentPageParser extends TournamentPageParser {

  var editors: List[Editor] = List.empty

  override def getEditors(html: String): List[Editor] = editors
}
