package com.atanana.parsers

import org.scalatest.{Matchers, WordSpecLike}

import scala.util.Success

class TournamentInfoParserTest extends WordSpecLike with Matchers {
  val parser = new TournamentInfoParser()

  "TournamentInfoParser" should {
    "parse questions count" in {
      val json =
        """
          |[
          |    {
          |        "idtournament": "3506",
          |        "name": "Чемпионат Перми и Пермского края",
          |        "town": "Пермь",
          |        "long_name": "XV Чемпионат Перми и Пермского края по игре \"Что? Где? Когда?\"",
          |        "date_start": "2015-11-08 14:00:00",
          |        "date_end": "2015-11-08 18:00:00",
          |        "tour_count": "4",
          |        "tour_questions": "12",
          |        "tour_ques_per_tour": "",
          |        "questions_total": "48",
          |        "type_name": "Обычный",
          |        "main_payment_value": "660",
          |        "main_payment_currency": "руб",
          |        "discounted_payment_value": "360",
          |        "discounted_payment_currency": "руб",
          |        "discounted_payment_reason": "для детских команд; 480 - для студенческих",
          |        "tournament_in_rating": "1",
          |        "date_requests_allowed_to": "",
          |        "comment": "",
          |        "site_url": "https://vk.com/chgk.perm.championship",
          |        "archive": "",
          |        "date_archived_at": "",
          |        "db_tags": []
          |    }
          |]
        """.stripMargin
      parser.getQuestionsCount(json) shouldEqual Success(48)
    }
  }
}
