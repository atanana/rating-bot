package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.TournamentInfo
import com.atanana.ratingbot.parsers.TournamentInfoParserImpl
import com.atanana.ratingbot.types.Pages.TournamentInfoPage
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.util.Success

class TournamentInfoParserTest extends AnyWordSpecLike with Matchers {
  val parser = new TournamentInfoParserImpl()

  "TournamentInfoParser" should {
    "parse questions count" in {
      val json =
        """
          |{
          |  "@context": "/contexts/Tournament",
          |  "@id": "/tournaments/7696",
          |  "@type": "Tournament",
          |  "id": 7696,
          |  "name": "XX ОВСЧ. 1 этап (синхрон)",
          |  "lastEditDate": "2022-10-02T14:54:59+03:00",
          |  "dateStart": "2022-09-29T18:00:00+03:00",
          |  "dateEnd": "2022-10-04T23:59:00+03:00",
          |  "type": {
          |    "@id": "/tournament_types/3",
          |    "@type": "TournamentType",
          |    "id": 3,
          |    "name": "Синхрон"
          |  },
          |  "season": "/seasons/57",
          |  "orgcommittee": [
          |    {
          |      "@id": "/players/97435",
          |      "@type": "Player",
          |      "id": 97435,
          |      "name": "Джияна",
          |      "patronymic": "Очировна",
          |      "surname": "Ичигеева"
          |    },
          |    {
          |      "@id": "/players/32901",
          |      "@type": "Player",
          |      "id": 32901,
          |      "name": "Наиль",
          |      "patronymic": "Евгеньевич",
          |      "surname": "Фарукшин"
          |    },
          |    {
          |      "@id": "/players/24850",
          |      "@type": "Player",
          |      "id": 24850,
          |      "name": "Александр",
          |      "patronymic": "Павлович",
          |      "surname": "Печеный"
          |    },
          |    {
          |      "@id": "/players/72160",
          |      "@type": "Player",
          |      "id": 72160,
          |      "name": "Александр",
          |      "patronymic": "Сергеевич",
          |      "surname": "Карчевский"
          |    },
          |    {
          |      "@id": "/players/505",
          |      "@type": "Player",
          |      "id": 505,
          |      "name": "Иделия",
          |      "patronymic": "Мукадясовна",
          |      "surname": "Айзятулова"
          |    }
          |  ],
          |  "synchData": {
          |    "@id": "/tournament_synches/7696",
          |    "@type": "TournamentSynch",
          |    "dateRequestsAllowedTo": "2022-10-04T11:59:00+03:00",
          |    "resultFixesTo": "2022-10-18T23:59:00+03:00",
          |    "resultsRecapsTo": "2022-10-07T23:55:00+03:00",
          |    "allowAppealCancel": true,
          |    "allowNarratorErrorAppeal": false,
          |    "archive": false,
          |    "dateDownloadQuestionsFrom": "2022-09-28T00:00:00+03:00",
          |    "dateDownloadQuestionsTo": "2022-10-04T19:30:00+03:00",
          |    "hideQuestionsTo": "2022-10-09T23:59:00+03:00",
          |    "hideResultsTo": "2022-10-09T23:59:00+03:00",
          |    "instantControversial": true
          |  },
          |  "mainPayment": 400,
          |  "discountedPayment": 0,
          |  "discountedPaymentReason": "Бесплатно играют школьные команды, а также команды по усмотрению оргкомитета",
          |  "currency": "r",
          |  "editors": [
          |    {
          |      "@id": "/players/13638",
          |      "@type": "Player",
          |      "id": 13638,
          |      "name": "Михаил",
          |      "patronymic": "Анатольевич",
          |      "surname": "Карпук"
          |    }
          |  ],
          |  "tournamentInRatingBalanced": true,
          |  "difficultyForecast": 4,
          |  "maiiAegis": true,
          |  "maiiAegisUpdatedAt": "2021-12-14T23:20:31.183623Z",
          |  "maiiRating": true,
          |  "maiiRatingUpdatedAt": "2021-12-14T23:20:31.183301Z",
          |  "questionQty": {
          |    "1": 12,
          |    "2": 12,
          |    "3": 12
          |  }
          |}
        """.stripMargin
      parser.getTournamentInfo(TournamentInfoPage(json)) shouldEqual Success(TournamentInfo("XX ОВСЧ. 1 этап (синхрон)", 36))
    }
  }
}
