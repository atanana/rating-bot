package com.atanana.parsers

import org.scalatest.{Matchers, WordSpecLike}
import resource._

import scala.io.Source
import scala.util.Success

class RequisitionsPageParserTest extends WordSpecLike with Matchers {
  "RequisitionsPageParser" should {
    "parse valid teams count" in {
      for (source <- managed(Source.fromFile("src/test/scala/com/atanana/parsers/requisitionsPage.html"))) {
        val html = source.getLines().mkString
        RequisitionsPageParser().additionalData("Кондратеня Андрей Александрович", html) shouldEqual Success(RequisitionAdditionalData("Минск", 1))
      }
    }
  }
}
