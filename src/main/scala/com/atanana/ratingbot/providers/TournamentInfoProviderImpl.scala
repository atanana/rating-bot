package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.{Editor, TournamentInfo}
import com.atanana.ratingbot.net.{Connector, ConnectorImpl}
import com.atanana.ratingbot.parsers.{TournamentInfoParser, TournamentPageParser, TournamentPageParserImpl}
import com.atanana.ratingbot.types.Ids.TournamentId

class TournamentInfoProviderImpl(
                                  connector: Connector,
                                  tournamentPageParser: TournamentPageParser,
                                  tournamentInfoParser: TournamentInfoParser
                                ) extends TournamentInfoProvider {

  override def getEditors(id: TournamentId): EitherT[IO, Throwable, List[Editor]] =
    for
      tournamentPage <- connector.getTournamentPage(id)
    yield tournamentPageParser.getEditors(tournamentPage)

  override def getInfo(id: TournamentId): EitherT[IO, Throwable, TournamentInfo] = for
    tournamentInfoPage <- connector.getTournamentInfo(id)
    tournamentInfo <- EitherT.fromEither[IO](tournamentInfoParser.getTournamentInfo(tournamentInfoPage).toEither)
  yield tournamentInfo
}
