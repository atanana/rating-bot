package com.atanana

import com.atanana.processors.Processor

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TestUtils {
  def getResult(processor: Processor): Either[String, Unit] = await(processor.process())

  def await[T](future: Future[T]): T = Await.result(future, Duration(1, TimeUnit.SECONDS))
}
