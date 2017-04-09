package com.atanana

import com.atanana.data.TournamentData
import org.scalatest.{Matchers, WordSpecLike}

class CsvParserTest extends WordSpecLike with Matchers {
  val parser = CsvParser()

  "CsvParser" should {
    "correct parse data" in {
      val data = StringContext.treatEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |4228;Чемпионат Беларуси;Минск;Обычный;2017-04-01 13:00:00;2017-04-02 15:00:00;4456;6;8,5;1043;998;-16;46;90 
        """.stripMargin)
      parser.getTournamentsData(data) shouldEqual List(TournamentData(4228, "Чемпионат Беларуси", "http://rating.chgk.info/tournament/4228", 8.5f, -16, 46))
    }

    "correct trim title row" in {
      val data = StringContext.treatEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |4228;Чемпионат Беларуси;Минск;Обычный;2017-04-01 13:00:00;2017-04-02 15:00:00;4456;6;8,5;1043;998;-16;46;90
        """.stripMargin)
      parser.getTournamentsData(data) should have size 1
    }

    "filter incorrect data" in {
      val data = StringContext.treatEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |test
        """.stripMargin)
      parser.getTournamentsData(data) shouldBe empty
    }

    "filter not ready results" in {
      val data = StringContext.treatEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |4228;Чемпионат Беларуси;Минск;Обычный;2017-04-01 13:00:00;2017-04-02 15:00:00;4456;6;9999;1043;998;-16;46;90
        """.stripMargin)
      parser.getTournamentsData(data) shouldBe empty
    }
  }
}
