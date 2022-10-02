package com.atanana

import cats.data.EitherT
import cats.implicits.catsStdInstancesForFuture
import com.atanana.TestUtils.fakeConfig
import com.atanana.posters.RealPoster
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import sttp.client3.UriContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RealPosterTest extends AnyFunSuite with MockFactory with Matchers {

  test("testPost") {
    val connector: Connector = mock[Connector]
    val poster: RealPoster = RealPoster(connector, fakeConfig)
    val message: String = "test message"

    val params = Map(
      "chat_id" -> 123.toString,
      "text" -> message,
      "disable_web_page_preview" -> "true",
      "parse_mode" -> "Markdown"
    )
    (connector.postAsync _).expects(uri"https://api.telegram.org/bottg%20token/sendMessage", params) returns EitherT.rightT[Future, Throwable]("123")

    poster.postAsync(message)
  }
}
