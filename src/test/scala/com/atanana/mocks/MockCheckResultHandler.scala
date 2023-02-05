package com.atanana.mocks

import cats.data.EitherT
import com.atanana.CheckResultHandler
import com.atanana.data.CheckResult

import scala.collection.mutable
import scala.concurrent.Future

class MockCheckResultHandler extends CheckResultHandler {

  val results: mutable.Map[CheckResult, EitherT[Future, Throwable, Unit]] = mutable.Map()

  override def processCheckResult(checkResult: CheckResult): EitherT[Future, Throwable, Unit] = results(checkResult)
}
