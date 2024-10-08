package com.atanana.ratingbot.json

import com.atanana.ratingbot.fs.{FsHandler, MockFsHandler}
import com.atanana.ratingbot.json.{Config, JsonConfig}
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.io.FileNotFoundException
import scala.util.{Failure, Success}

class JsonConfigTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {
  private val fsHandler = new MockFsHandler()
  private val jsonConfig = JsonConfig(fsHandler)

  after {
    fsHandler.clear()
  }

  "JsonConfig" should {
    "read config" in {
      fsHandler.writeFile(
        """{
          |  "tgToken": "tg token",
          |  "authCookie": "test cookie",
          |  "chat": 1,
          |  "team": 2,
          |  "city": 3,
          |  "country": 4,
          |  "ignoredVenues": ["test", "test2"],
          |  "pipe": "/opt/rating_pipe"
          |}""".stripMargin, "config.json")
      jsonConfig.read shouldEqual Success(Config("tg token", "test cookie", 1, 2, 3, "/opt/rating_pipe", 4, List("test", "test2")))
    }

    "not fails on reading config" in {
      jsonConfig.read shouldBe a[Failure[_]]
    }
  }
}
