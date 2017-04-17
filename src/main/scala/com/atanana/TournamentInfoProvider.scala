package com.atanana

import javax.inject.Inject

import com.atanana.data.Editor
import com.atanana.parsers.TournamentPageParser

class TournamentInfoProvider @Inject()(connector: Connector, tournamentPageParser: TournamentPageParser) {
  def getEditors(id: Int): List[Editor] = {
    val tournamentPage = connector.getTournamentPage(id)
    tournamentPageParser.getEditors(tournamentPage)
  }
}
