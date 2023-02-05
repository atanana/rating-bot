package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.Editor
import com.atanana.net.{Connector, ConnectorImpl}
import com.atanana.parsers.{TournamentPageParser, TournamentPageParserImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TournamentInfoProviderImpl(connector: Connector, tournamentPageParser: TournamentPageParser) extends TournamentInfoProvider {

  override def getEditors(id: Int): EitherT[Future, Throwable, List[Editor]] =
    for {
      tournamentPage <- connector.getTournamentPage(id)
    } yield tournamentPageParser.getEditors(tournamentPage)
}
