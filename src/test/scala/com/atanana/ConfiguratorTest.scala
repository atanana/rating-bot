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

      configurator.config shouldEqual Success(Config("test token", 123, 321))
    }

    "fail without token" in {
      (systemWrapper.get _).when("token").returns("")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("321")

      configurator.config shouldBe a[Failure[_]]
    }

    "fail without chat" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("")
      (systemWrapper.get _).when("team").returns("321")

      configurator.config shouldBe a[Failure[_]]
    }

    "fail without team" in {
      (systemWrapper.get _).when("token").returns("test token")
      (systemWrapper.get _).when("chat").returns("123")
      (systemWrapper.get _).when("team").returns("")

      configurator.config shouldBe a[Failure[_]]
    }
  }
}
