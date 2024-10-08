package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.TestUtils.fakeConfig
import com.atanana.ratingbot.net.MockConnector
import com.atanana.ratingbot.posters.RealPoster
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import sttp.client3.UriContext

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
    connector.postResponses.put((uri"https://api.telegram.org/bottg%20token/sendMessage", params), EitherT.rightT("123"))

    poster.postAsync(message)
  }
}
