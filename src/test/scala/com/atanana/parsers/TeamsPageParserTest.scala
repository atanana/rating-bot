package com.atanana.parsers

import com.atanana.data.Team
import org.scalatest.{Matchers, WordSpecLike}

import scala.io.Source

//noinspection SourceNotClosed
class TeamsPageParserTest extends WordSpecLike with Matchers {
  val parser = new TeamsPageParser
  private val html = Source.fromFile("src/test/scala/com/atanana/parsers/allTeams.html", "cp1251").getLines().mkString
  private val teams = parser.getTeams(html)

  "TeamsPageParser" should {
    "parse valid teams count" in {
      teams should have size 596
    }

    "parse valid real team" in {
      teams.head shouldEqual Team(45556, "Рабочее название", "Санкт-Петербург", 9921, 1.0f)
    }

    "parse valid virtual team" in {
      teams(13) shouldEqual Team(64861, "Рислинг", "сборная", 7381, 10.5f, isReal = false)
    }
  }
}
