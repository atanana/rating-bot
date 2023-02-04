package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.Editor
import com.atanana.net.Connector
import com.atanana.parsers.TournamentPageParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TournamentInfoProvider(connector: Connector, tournamentPageParser: TournamentPageParser) {

  def getEditors(id: Int): EitherT[Future, Throwable, List[Editor]] =
    for {
      tournamentPage <- connector.getTournamentPage(id)
    } yield tournamentPageParser.getEditors(tournamentPage)
}
