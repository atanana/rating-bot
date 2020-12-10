package com.atanana

import com.atanana.processors.Processor

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

//noinspection ScalaDeprecation
object TestUtils {

  def getResult(processor: Processor): Either[Throwable, Unit] = await(processor.process().value)

  def getResultErrorMessage(processor: Processor): String = await(processor.process().value).left.get.getMessage

  def await[T](future: Future[T]): T = Await.result(future, Duration(1, TimeUnit.SECONDS))
}
