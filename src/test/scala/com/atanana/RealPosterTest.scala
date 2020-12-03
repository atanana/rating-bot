package com.atanana

import com.atanana.json.Config
import com.atanana.posters.RealPoster
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import sttp.client3.UriContext

import scala.collection.immutable.Map

class RealPosterTest extends FunSuite with MockFactory with Matchers {
  test("testPost") {
    val connector: Connector = mock[Connector]
    val poster: RealPoster = RealPoster(connector, Config("token", 123, 321, 456, 10000, "test city", "test country", List.empty))
    val message: String = "test message"

    val params = Map(
      "chat_id" -> 123.toString,
      "text" -> message,
      "disable_web_page_preview" -> "true",
      "parse_mode" -> "Markdown"
    )
    (connector.post _).expects(uri"https://api.telegram.org/bottoken/sendMessage", params) returns Left("error")

    poster.post(message)
  }
}
