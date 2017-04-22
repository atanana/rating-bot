package com.atanana

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class ConfiguratorTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  var systemWrapper: SystemWrapper = _
  var configurator: Configurator = _

  before {
    systemWrapper = stub[SystemWrapper]
    configurator = Configurator(systemWrapper)
  }

  "Configurator" should {
    "correct parse config" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("321")
      (systemWrapper.get _).when("city").returns("456")
      (systemWrapper.get _).when("port").returns("10000")

      configurator.config shouldEqual Success(Config("test token", 123, 321, 456, 10000))
    }

    "fail without token" in {
      (systemWrapper.get _).when("token").returns("")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("321")
      (systemWrapper.get _).when("city").returns("456")

      configurator.config shouldBe a[Failure[_]]
    }

    "fail without chat" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("")
      (systemWrapper.get _).when("team").returns("321")
      (systemWrapper.get _).when("city").returns("456")

      configurator.config shouldBe a[Failure[_]]
    }

    "fail without team" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("")
      (systemWrapper.get _).when("city").returns("456")

      configurator.config shouldBe a[Failure[_]]
    }

    "fail without city" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("321")
      (systemWrapper.get _).when("city").returns("")

      configurator.config shouldBe a[Failure[_]]
    }

    "set default port" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("321")
      (systemWrapper.get _).when("city").returns("456")
      (systemWrapper.get _).when("port").returns("")

      configurator.config.get.port shouldEqual 11000
    }
  }
}
