package com.atanana.parsers

import com.atanana.data.Team
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.io.Source

//noinspection SourceNotClosed
class TeamsPageParserTest extends AnyWordSpecLike with Matchers {
  val parser = new TeamsPageParser
  private val content = Source.fromFile("src/test/scala/com/atanana/parsers/allTeams.json").getLines().mkString
  private val teams = parser.getTeams(content)

  "TeamsPageParser" should {
    "parse valid teams count" in {
      teams should have size 100
    }

    "parse valid team" in {
      teams.head shouldEqual Team(45556, "Рабочее название", "Санкт-Петербург", 12088, 1.0f)
    }
  }
}
