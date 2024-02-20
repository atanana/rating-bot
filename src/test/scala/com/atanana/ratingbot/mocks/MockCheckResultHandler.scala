package com.atanana.ratingbot.mocks

import cats.data.EitherT
import com.atanana.ratingbot.CheckResultHandler
import com.atanana.ratingbot.data.CheckResult

import scala.collection.mutable
import scala.concurrent.Future

class MockCheckResultHandler extends CheckResultHandler {

  val results: mutable.Map[CheckResult, EitherT[Future, Throwable, Unit]] = mutable.Map()

  override def processCheckResult(checkResult: CheckResult): EitherT[Future, Throwable, Unit] = results(checkResult)
}
