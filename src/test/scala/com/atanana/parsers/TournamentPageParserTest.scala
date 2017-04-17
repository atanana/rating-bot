package com.atanana.parsers

import com.atanana.data.Editor
import org.scalatest.{Matchers, WordSpecLike}

import scala.io.Source

class TournamentPageParserTest extends WordSpecLike with Matchers {
  "TournamentPageParser" should {
    "provide correct editors data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/editorsTest.html", "cp1251").getLines().mkString
      TournamentPageParser().getEditors(html) shouldEqual List(
        Editor("Демьянцев Игорь Николаевич – команда Горячие головы (Гомель)"),
        Editor("Кожедуб Олег Анатольевич – команда Ультиматум (Гомель)")
      )
    }
  }
}
