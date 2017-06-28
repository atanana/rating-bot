package com.atanana

import com.atanana.json.Config
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.Map
import scalaj.http.HttpResponse

class PosterTest extends FunSuite with MockFactory with Matchers {
  test("testPost") {
    val connector: Connector = mock[Connector]
    val poster: Poster = Poster(connector, Config("token", 123, 321, 456, 10000, "test city", "test country"))
    val message: String = "test message"

    (connector.get _).expects("https://api.vk.com/method/messages.send?chat_id=123&message=test+message&access_token=token&v=5.57").returns(HttpResponse("test", 0, Map.empty))

    poster.post(message)
  }
}
