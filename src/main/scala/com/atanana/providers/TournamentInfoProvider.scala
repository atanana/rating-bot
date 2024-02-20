package com.atanana.providers

import cats.data.EitherT
import com.atanana.data.{Editor, TournamentInfo}
import com.atanana.types.Ids.TournamentId

import scala.concurrent.Future

trait TournamentInfoProvider {

  def getEditors(id: TournamentId): EitherT[Future, Throwable, List[Editor]] //todo

  def getInfo(id: TournamentId): EitherT[Future, Throwable, TournamentInfo]
}
