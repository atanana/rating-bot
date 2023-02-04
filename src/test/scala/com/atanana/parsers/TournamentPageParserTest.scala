package com.atanana.parsers

import com.atanana.data.Editor
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.io.Source

//noinspection SourceNotClosed
class TournamentPageParserTest extends AnyWordSpecLike with Matchers {
  "TournamentPageParser" should {
    "provide correct editors data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/editorsTest.html", "cp1251").getLines().mkString
      TournamentPageParserImpl().getEditors(html) shouldEqual List(
        Editor("Демьянцев Игорь Николаевич – команда Горячие головы (Гомель)"),
        Editor("Кожедуб Олег Анатольевич – команда Ультиматум (Гомель)")
      )
    }
  }
}
