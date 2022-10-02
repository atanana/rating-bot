package com.atanana.json

import com.atanana.FsHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.io.FileNotFoundException
import scala.util.{Failure, Success}

class JsonConfigTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val fsHandler = stub[FsHandler]
  private val jsonConfig = JsonConfig(fsHandler)

  "JsonConfig" should {
    "read config" in {
      (fsHandler.readFile _).when(JsonConfig.FILE_NAME).returns(Success(
        """{
          |  "token": "test token",
          |  "authCookie": "test cookie",
          |  "chat": 1,
          |  "team": 2,
          |  "city": 3,
          |  "cityName": "Минск",
          |  "countryName": "Беларусь",
          |  "ignoredVenues": ["test", "test2"],
          |  "port": 11000
          |}""".stripMargin
      ))
      jsonConfig.read shouldEqual Success(Config("test token", "test cookie", 1, 2, 3, 11000, "Минск", "Беларусь", List("test", "test2")))
    }

    "not fails on reading config" in {
      (fsHandler.readFile _).when(JsonConfig.FILE_NAME).returns(Failure(new FileNotFoundException()))
      jsonConfig.read shouldBe a[Failure[_]]
    }
  }
}
