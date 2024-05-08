package com.atanana.ratingbot.providers

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.{Editor, TournamentInfo}
import com.atanana.ratingbot.types.Ids.TournamentId

trait TournamentInfoProvider {

  def getEditors(id: TournamentId): EitherT[IO, Throwable, List[Editor]] //todo

  def getInfo(id: TournamentId): EitherT[IO, Throwable, TournamentInfo]
}
