package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.CheckResultHandler
import com.atanana.ratingbot.data.CheckResult

import scala.collection.mutable

class MockCheckResultHandler extends CheckResultHandler {

  val results: mutable.Map[CheckResult, EitherT[IO, Throwable, Unit]] = mutable.Map()

  override def processCheckResult(checkResult: CheckResult): EitherT[IO, Throwable, Unit] = results(checkResult)
}
