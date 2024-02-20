package com.atanana.parsers

import com.atanana.ratingbot.types.Pages.TournamentResultsPage
import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.Conversions.fromIntToTeamId
import com.atanana.ratingbot.data.TournamentResult
import com.atanana.ratingbot.parsers.TournamentResultsParserImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.util.Success

class TournamentResultsParserImplTest extends AnyWordSpecLike with Matchers {

  private val parser = new TournamentResultsParserImpl()

  "TournamentResultsParserImpl" should {

    "parse team results" in {
      val json =
        """[
          |  {
          |    "team": {
          |      "id": 6936,
          |      "name": "Хронически разумные United",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "current": {
          |      "name": "Хронически разумные United",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "questionsTotal": 32,
          |    "synchRequest": null,
          |    "rating": {
          |      "inRating": true,
          |      "b": 1922,
          |      "predictedPosition": 3,
          |      "rt": 11029,
          |      "rb": 11101,
          |      "rg": 10989,
          |      "r": 11060,
          |      "bp": 1808,
          |      "d1": 114,
          |      "d2": 102,
          |      "d": 216
          |    },
          |    "position": 1
          |  },
          |  {
          |    "team": {
          |      "id": 51739,
          |      "name": "Зоопарк",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "current": {
          |      "name": "Зоопарк",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "questionsTotal": 31,
          |    "synchRequest": null,
          |    "rating": {
          |      "inRating": true,
          |      "b": 1826,
          |      "predictedPosition": 2,
          |      "rt": 10621,
          |      "rb": 11109,
          |      "rg": 11018,
          |      "r": 11524,
          |      "bp": 1826,
          |      "d1": 0,
          |      "d2": 77,
          |      "d": 77
          |    },
          |    "position": 2
          |  },
          |  {
          |    "team": {
          |      "id": 4252,
          |      "name": "Ять",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "current": {
          |      "name": "Ять",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "questionsTotal": 30,
          |    "synchRequest": null,
          |    "rating": {
          |      "inRating": true,
          |      "b": 1792,
          |      "predictedPosition": 10,
          |      "rt": 9133,
          |      "rb": 9133,
          |      "rg": 9216,
          |      "r": 9216,
          |      "bp": 1492,
          |      "d1": 300,
          |      "d2": 70,
          |      "d": 370
          |    },
          |    "position": 3.5
          |  },
          |  {
          |    "team": {
          |      "id": 7864,
          |      "name": "Одушевлённые аэросани",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "current": {
          |      "name": "Одушевлённые аэросани",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "questionsTotal": 30,
          |    "synchRequest": null,
          |    "rating": {
          |      "inRating": true,
          |      "b": 1792,
          |      "predictedPosition": 1,
          |      "rt": 11780,
          |      "rb": 11780,
          |      "rg": 12055,
          |      "r": 12055,
          |      "bp": 1922,
          |      "d1": -65,
          |      "d2": 70,
          |      "d": 5
          |    },
          |    "position": 3.5
          |  }
          |]""".stripMargin

      parser.getTeamResults(TournamentResultsPage(json), 10292, 7864) shouldEqual Success(Some(
        TournamentResult(10292, 30, 3.5, 5)
      ))
    }

    "should return no result when no rating" in {
      val json =
        """[
          |  {
          |    "team": {
          |      "id": 6936,
          |      "name": "Хронически разумные United",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "current": {
          |      "name": "Хронически разумные United",
          |      "town": {
          |        "id": 197,
          |        "name": "Минск"
          |      }
          |    },
          |    "questionsTotal": 32,
          |    "synchRequest": null,
          |    "rating": {
          |      "inRating": false,
          |      "b": 1922,
          |      "predictedPosition": 3,
          |      "rt": 11029,
          |      "rb": 11101,
          |      "rg": 10989,
          |      "r": 11060,
          |      "bp": 1808,
          |      "d1": 114,
          |      "d2": 102,
          |      "d": 216
          |    },
          |    "position": 1
          |  }
          |]""".stripMargin

      parser.getTeamResults(TournamentResultsPage(json), 9700, 6936) shouldEqual Success(None)
    }
  }
}
