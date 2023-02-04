package com.atanana

import cats.data.EitherT
import com.atanana.data.CheckResult

import scala.concurrent.Future

trait CheckResultHandler {

  def processCheckResult(checkResult: CheckResult): EitherT[Future, Throwable, Unit]
}
