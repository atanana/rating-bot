package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.data.CheckResult

trait CheckResultHandler {

  def processCheckResult(checkResult: CheckResult): EitherT[IO, Throwable, Unit]
}
