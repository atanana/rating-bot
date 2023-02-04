package com.atanana.mocks

import cats.data.EitherT
import com.atanana.processors.{PollProcessor, ReminderProcessor, TeamPositionsProcessor}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MockProcessor extends PollProcessor with ReminderProcessor with TeamPositionsProcessor {

  var invocationsCount = 0

  override def process(): EitherT[Future, Throwable, Unit] = {
    invocationsCount += 1
    EitherT.rightT[Future, Throwable](())
  }

  def reset(): Unit = invocationsCount = 0
}
