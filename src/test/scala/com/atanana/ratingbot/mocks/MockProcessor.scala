package com.atanana.ratingbot.mocks

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.processors.{PollProcessor, ReminderProcessor, TeamPositionsProcessor}

import scala.concurrent.ExecutionContext.Implicits.global

class MockProcessor extends PollProcessor with ReminderProcessor with TeamPositionsProcessor {

  var invocationsCount = 0

  override def process(): EitherT[IO, Throwable, Unit] = {
    invocationsCount += 1
    EitherT.rightT(())
  }

  def reset(): Unit = invocationsCount = 0
}
