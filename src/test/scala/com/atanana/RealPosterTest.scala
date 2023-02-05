package com.atanana

import cats.data.EitherT
import cats.implicits.catsStdInstancesForFuture
import com.atanana.TestUtils.fakeConfig
import com.atanana.net.MockConnector
import com.atanana.posters.RealPoster
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import sttp.client3.UriContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RealPosterTest extends AnyFunSuite with Matchers {

  test("testPost") {
    val connector = new MockConnector()
    val poster: RealPoster = RealPoster(connector, fakeConfig)
    val message: String = "test message"

    val params = Map(
      "chat_id" -> 123.toString,
      "text" -> message,
      "disable_web_page_preview" -> "true",
      "parse_mode" -> "Markdown"
    )
    connector.postResponses.put((uri"https://api.telegram.org/bottg%20token/sendMessage", params), EitherT.rightT[Future, Throwable]("123"))

    poster.postAsync(message)
  }
}
