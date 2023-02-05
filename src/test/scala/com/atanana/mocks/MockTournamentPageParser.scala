package com.atanana.mocks

import com.atanana.data.Editor
import com.atanana.parsers.TournamentPageParser

class MockTournamentPageParser extends TournamentPageParser {

  var editors: List[Editor] = List.empty

  override def getEditors(html: String): List[Editor] = editors
}
