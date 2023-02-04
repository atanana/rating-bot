package com.atanana.mocks

import cats.data.EitherT
import com.atanana.data.Editor
import com.atanana.providers.TournamentInfoProvider

import scala.concurrent.Future

class MockTournamentInfoProvider extends TournamentInfoProvider {

  var editors: EitherT[Future, Throwable, List[Editor]] = _

  override def getEditors(id: Int): EitherT[Future, Throwable, List[Editor]] = editors
}
