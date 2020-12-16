package com.atanana.parsers

import com.atanana.data.TournamentData
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class CsvParserTest extends AnyWordSpecLike with Matchers {
  private val parser = CsvParser()

  "CsvParser" should {
    "correct parse data" in {
      val data = StringContext.processEscapes(
        """
          |"Ид","Турнир","Город","Тип","С","По","RG","MP","M","BP","B","D","Взято","Из"
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) shouldEqual List(TournamentData(5756, "Жизнь и время Михаэля К.", "https://rating.chgk.info/tournament/5756", 24f, 116, 23))
    }

    "correct trim title row" in {
      val data = StringContext.processEscapes(
        """
          |"Ид","Турнир","Город","Тип","С","По","RG","MP","M","BP","B","D","Взято","Из"
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) should have size 1
    }

    "filter incorrect data" in {
      val data = StringContext.processEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |test
        """.stripMargin)
      parser.getTournamentsData(data) shouldBe empty
    }

    "filter not ready results" in {
      val data = StringContext.processEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","9999","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) shouldBe empty
    }

    "filter not interesting tournaments" in {
      val data = StringContext.processEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |"5756","Жизнь и время Михаэля К.","","Общий зачёт","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) shouldBe empty
    }
  }
}
