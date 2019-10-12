package com.atanana.json

import java.io.FileNotFoundException

import com.atanana.FsHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class JsonConfigTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var fsHandler: FsHandler = _
  var jsonConfig: JsonConfig = _

  before {
    fsHandler = stub[FsHandler]
    jsonConfig = JsonConfig(fsHandler)
  }

  "JsonConfig" should {
    "read config" in {
      (fsHandler.readFile _).when(JsonConfig.FILE_NAME).returns(Success(
        """{
          |  "token": "test token",
          |  "chat": 1,
          |  "team": 2,
          |  "city": 3,
          |  "cityName": "Минск",
          |  "countryName": "Беларусь",
          |  "ignoredVenues": ["test", "test2"],
          |  "port": 11000
          |}""".stripMargin
      ))
      jsonConfig.read shouldEqual Success(Config("test token", 1, 2, 3, 11000, "Минск", "Беларусь", List("test", "test2")))
    }

    "not fails on reading config" in {
      (fsHandler.readFile _).when(JsonConfig.FILE_NAME).returns(Failure(new FileNotFoundException()))
      jsonConfig.read shouldBe a[Failure[_]]
    }
  }
}
