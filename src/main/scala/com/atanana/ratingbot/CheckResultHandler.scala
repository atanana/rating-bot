package com.atanana.ratingbot

import cats.data.EitherT
import com.atanana.ratingbot.data.CheckResult

import scala.concurrent.Future

trait CheckResultHandler {

  def processCheckResult(checkResult: CheckResult): EitherT[Future, Throwable, Unit]
}
