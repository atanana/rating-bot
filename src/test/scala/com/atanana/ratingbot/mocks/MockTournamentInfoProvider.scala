package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.{Editor, TournamentInfo}
import com.atanana.ratingbot.providers.TournamentInfoProvider
import com.atanana.ratingbot.types.Ids.TournamentId

import scala.collection.mutable

class MockTournamentInfoProvider extends TournamentInfoProvider {

  val tournamentInfos: mutable.Map[TournamentId, EitherT[IO, Throwable, TournamentInfo]] = mutable.Map()
  var editors: mutable.Map[TournamentId, EitherT[IO, Throwable, List[Editor]]] = mutable.Map()

  override def getEditors(id: TournamentId): EitherT[IO, Throwable, List[Editor]] = editors(id)

  override def getInfo(id: TournamentId): EitherT[IO, Throwable, TournamentInfo] = tournamentInfos(id)
}
