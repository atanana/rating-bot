package com.atanana

import cats.data.EitherT
import com.atanana.json.Config
import com.atanana.processors.Processor

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

//noinspection ScalaDeprecation
object TestUtils {

  val fakeConfig: Config = Config("tg token", "api token", "cookie", 123, 321, 456, 10000, 1, List.empty)

  def getResult(processor: Processor): Either[Throwable, Unit] = await(processor.process().value)

  def getResultErrorMessage(processor: Processor): String = await(processor.process().value).left.get.getMessage

  def awaitError[T <: Throwable](either: EitherT[Future, T, _]): T = awaitEither(either).left.get

  def awaitEither[L, R](either: EitherT[Future, L, R]): Either[L, R] = await(either.value)

  def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
