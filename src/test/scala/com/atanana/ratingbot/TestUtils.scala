package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.processors.Processor
import org.scalatest.Assertions.fail

import scala.concurrent.duration.*

import cats.effect.unsafe.implicits.global

//noinspection ScalaDeprecation
object TestUtils {

  val fakeConfig: Config = Config("tg token", "cookie", 123, 321, 456, 10000, 1, List.empty)

  def getResult(processor: Processor): Either[Throwable, Unit] = await(processor.process().value)

  def getResultErrorMessage(processor: Processor): String = await(processor.process().value).swap.getOrElse(fail("Either is not failed!")).getMessage

  def awaitError[T <: Throwable](either: EitherT[IO, T, _]): T = awaitEither(either).swap.getOrElse(fail("Either is not failed!"))

  def awaitEither[L, R](either: EitherT[IO, L, R]): Either[L, R] = await(either.value)

  def await[T](io: IO[T]): T = io.unsafeRunSync() //todo
}
