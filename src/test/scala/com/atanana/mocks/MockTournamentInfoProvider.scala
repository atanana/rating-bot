package com.atanana.mocks

import cats.data.EitherT
import com.atanana.data.Editor
import com.atanana.providers.TournamentInfoProvider

import scala.collection.mutable
import scala.concurrent.Future

class MockTournamentInfoProvider extends TournamentInfoProvider {

  var editors: mutable.Map[Int, EitherT[Future, Throwable, List[Editor]]] = mutable.Map[Int, EitherT[Future, Throwable, List[Editor]]]()

  override def getEditors(id: Int): EitherT[Future, Throwable, List[Editor]] = editors(id)
}
