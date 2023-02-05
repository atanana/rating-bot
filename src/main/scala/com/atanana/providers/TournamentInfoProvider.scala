package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.Editor

import scala.concurrent.Future

trait TournamentInfoProvider {

  def getEditors(id: Int): EitherT[Future, Throwable, List[Editor]]
}
