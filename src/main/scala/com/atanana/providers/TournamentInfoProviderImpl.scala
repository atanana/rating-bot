package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.{Editor, TournamentInfo}
import com.atanana.net.{Connector, ConnectorImpl}
import com.atanana.parsers.{TournamentInfoParser, TournamentPageParser, TournamentPageParserImpl}
import com.atanana.types.Ids.TournamentId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TournamentInfoProviderImpl(
                                  connector: Connector,
                                  tournamentPageParser: TournamentPageParser,
                                  tournamentInfoParser: TournamentInfoParser
                                ) extends TournamentInfoProvider {

  override def getEditors(id: TournamentId): EitherT[Future, Throwable, List[Editor]] =
    for
      tournamentPage <- connector.getTournamentPage(id)
    yield tournamentPageParser.getEditors(tournamentPage)

  override def getInfo(id: TournamentId): EitherT[Future, Throwable, TournamentInfo] = for
    tournamentInfoPage <- connector.getTournamentInfo(id)
    tournamentInfo <- EitherT.fromEither[Future](tournamentInfoParser.getTournamentInfo(tournamentInfoPage).toEither)
  yield tournamentInfo
}
