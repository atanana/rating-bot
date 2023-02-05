package com.atanana.parsers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.io.Source
import scala.util.{Success, Using}

class RequisitionsPageParserTest extends AnyWordSpecLike with Matchers {

  "RequisitionsPageParser" should {

    "parse valid teams count" in {
      val html = Using.resource(Source.fromFile("src/test/scala/com/atanana/parsers/requisitionsPage.html")) {
        _.getLines().mkString
      }
      RequisitionsPageParserImpl().additionalData("Кондратеня Андрей Александрович", html) shouldEqual Success(RequisitionAdditionalData("Минск", 1))
    }
  }
}
