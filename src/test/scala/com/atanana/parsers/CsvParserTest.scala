package com.atanana.parsers

import com.atanana.data.TournamentData
import com.atanana.providers.TournamentPollingFilter
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class CsvParserTest extends AnyWordSpecLike with Matchers with MockFactory {
  private val filter = stub[TournamentPollingFilter]
  private val parser = new CsvParser(filter)

  "CsvParser" should {
    "correct parse data" in {
      (filter.isInteresting _).when(*, *).returns(true)
      val data = StringContext.processEscapes(
        """
          |"Ид","Турнир","Город","Тип","С","По","RG","MP","M","BP","B","D","Взято","Из"
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) shouldEqual List(TournamentData(5756, "Жизнь и время Михаэля К.", "https://rating.chgk.info/tournament/5756", 24f, 116, 23))
    }

    "correct trim title row" in {
      (filter.isInteresting _).when(*, *).returns(true)
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

    "filter results" in {
      (filter.isInteresting _).when("Синхрон", *).returns(false)
      val data = StringContext.processEscapes(
        """
          |Ид;Турнир;Город;Тип;С;По;RG;MP;M;BP;B;D;Взято;Из \r
          |"5756","Жизнь и время Михаэля К.","","Синхрон","2019-10-03 19:00:00","2019-10-09 19:00:00","6608","55","24","1596","1764","116","23","36"
        """.stripMargin)
      parser.getTournamentsData(data) shouldBe empty
    }
  }
}
