package com.atanana.ratingbot.mocks

import cats.data.EitherT
import com.atanana.ratingbot.processors.{PollProcessor, ReminderProcessor, TeamPositionsProcessor}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MockProcessor extends PollProcessor with ReminderProcessor with TeamPositionsProcessor {

  var invocationsCount = 0

  override def process(): EitherT[Future, Throwable, Unit] = {
    invocationsCount += 1
    EitherT.rightT[Future, Throwable](())
  }

  def reset(): Unit = invocationsCount = 0
}
