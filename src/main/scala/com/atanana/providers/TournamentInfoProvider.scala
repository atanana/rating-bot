package com.atanana.providers

import cats.data.EitherT
import com.atanana.Connector
import com.atanana.data.Editor
import com.atanana.parsers.TournamentPageParser

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TournamentInfoProvider @Inject()(connector: Connector, tournamentPageParser: TournamentPageParser) {

  def getEditors(id: Int): EitherT[Future, Throwable, List[Editor]] =
    for {
      tournamentPage <- connector.getTournamentPage(id)
    } yield tournamentPageParser.getEditors(tournamentPage)
}
