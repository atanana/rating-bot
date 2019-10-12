package com.atanana.parsers

import com.atanana.data.TournamentData
import org.scalatest.{Matchers, WordSpecLike}

class CsvParserTest extends WordSpecLike with Matchers {
  val parser = CsvParser()

  "CsvParser" should {
    "correct parse data" in {
      val data = StringContext.treatEscapes(
        """
          |"Ид","Турнир","Город","Тип","С","По","RG","MP","M","BP","B","D","Взято","Из"
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) shouldEqual List(TournamentData(5756, "Жизнь и время Михаэля К.", "https://rating.chgk.info/tournament/5756", 24f, 116, 23))
    }

    "correct trim title row" in {
      val data = StringContext.treatEscapes(
        """
          |"Ид","Турнир","Город","Тип","С","По","RG","MP","M","BP","B","D","Взято","Из"
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
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
