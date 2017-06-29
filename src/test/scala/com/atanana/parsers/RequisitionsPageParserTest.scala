package com.atanana.parsers

import org.scalatest.{Matchers, WordSpecLike}

import scala.io.Source
import scala.util.Success

class RequisitionsPageParserTest extends WordSpecLike with Matchers {
  "RequisitionsPageParser" should {
    "parse valid teams count" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/requisitionsPage.html", "cp1251").getLines().mkString
      RequisitionsPageParser().teamsCount("Топчий Иван Александрович", html) shouldEqual Success(5)
    }
  }
}
