package com.atanana.ratingbot

import com.atanana.ratingbot.Conversions.fromIntToTournamentId
import com.atanana.ratingbot.data.{Data, Tournament}
import com.atanana.ratingbot.fs.MockFsHandler
import com.atanana.ratingbot.json.JsonStoreImpl
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class JsonStoreTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {
  private val fsHandler = new MockFsHandler()
  private val jsonStore = new JsonStoreImpl(fsHandler)

  after {
    fsHandler.clear()
  }

  "JsonStore" should {
    "write correct data" in {
      val tournaments = Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1))
      val data = Data(tournaments)
      val json =
        """{
          |  "tournaments": [{
          |    "id": 1,
          |    "score": 3
          |  }, {
          |    "id": 2,
          |    "score": 2
          |  }, {
          |    "id": 3,
          |    "score": 1
          |  }]
          |}""".stripMargin
      jsonStore.write(data)
      fsHandler.readFile("data.json").get shouldEqual json
    }

    "read data" in {
      fsHandler.writeFile(
        """{
          |  "tournaments": [{
          |    "id": 1,
          |    "score": 3
          |  }, {
          |    "id": 2,
          |    "score": 2
          |  }, {
          |    "id": 3,
          |    "score": 1
          |  }]
          |}""".stripMargin, "data.json")
      jsonStore.read shouldEqual Data(
        Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1))
      )
    }

    "return empty data when no file" in {
      jsonStore.read shouldEqual Data(Set.empty)
    }

    "return empty data when invalid json in file" in {
      fsHandler.writeFile("test", "data.json")
      jsonStore.read shouldEqual Data(Set.empty)
    }
  }
}
