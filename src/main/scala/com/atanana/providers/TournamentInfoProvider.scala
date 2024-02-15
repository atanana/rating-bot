package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.Editor
import com.atanana.types.Ids.TournamentId

import scala.concurrent.Future

trait TournamentInfoProvider {

  def getEditors(id: TournamentId): EitherT[Future, Throwable, List[Editor]]
}
