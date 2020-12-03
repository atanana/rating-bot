package com.atanana.providers

import javax.inject.Inject

import com.atanana.Connector
import com.atanana.data.Editor
import com.atanana.parsers.TournamentPageParser

class TournamentInfoProvider @Inject()(connector: Connector, tournamentPageParser: TournamentPageParser) {
  def getEditors(id: Int): Either[String, List[Editor]] = {
    connector.getTournamentPage(id).map(tournamentPageParser.getEditors)
  }
}
