package com.atanana.mocks

import cats.data.EitherT
import com.atanana.data.Editor
import com.atanana.providers.TournamentInfoProvider
import com.atanana.types.Ids.TournamentId

import scala.collection.mutable
import scala.concurrent.Future

class MockTournamentInfoProvider extends TournamentInfoProvider {

  var editors: mutable.Map[TournamentId, EitherT[Future, Throwable, List[Editor]]] = mutable.Map()

  override def getEditors(id: TournamentId): EitherT[Future, Throwable, List[Editor]] = editors(id)
}
