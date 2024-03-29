package com.atanana.ratingbot

import cats.data.EitherT
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.processors.Processor
import org.scalatest.Assertions.fail

import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}

//noinspection ScalaDeprecation
object TestUtils {

  val fakeConfig: Config = Config("tg token", "cookie", 123, 321, 456, 10000, 1, List.empty)

  def getResult(processor: Processor): Either[Throwable, Unit] = await(processor.process().value)

  def getResultErrorMessage(processor: Processor): String = await(processor.process().value).swap.getOrElse(fail("Either is not failed!")).getMessage

  def awaitError[T <: Throwable](either: EitherT[Future, T, _]): T = awaitEither(either).swap.getOrElse(fail("Either is not failed!"))

  def awaitEither[L, R](either: EitherT[Future, L, R]): Either[L, R] = await(either.value)

  def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
