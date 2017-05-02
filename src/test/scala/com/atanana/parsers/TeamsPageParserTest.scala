package com.atanana.parsers

import com.atanana.data.Team
import org.scalatest.{Matchers, WordSpecLike}

import scala.io.Source

class TeamsPageParserTest extends WordSpecLike with Matchers {
  val parser = new TeamsPageParser

  "TeamsPageParser" should {
    "provide valid teams data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/allTeams.html", "cp1251").getLines().mkString
      val teams = parser.getTeams(html)
      teams should have size 500
      teams.head shouldEqual Team(45556, "Рабочее название", "Санкт-Петербург", 11196, 1.0f)
    }
  }
}
