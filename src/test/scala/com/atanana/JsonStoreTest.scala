package com.atanana

import com.atanana.data.{Data, Requisition, Tournament}
import com.atanana.fs.{FsHandlerImpl, MockFsHandler}
import com.atanana.json.JsonStoreImpl
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import com.atanana.Conversions.fromIntToTournamentId

import java.time.LocalDateTime
import scala.util.{Failure, Success}

class JsonStoreTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {
  private val fsHandler = new MockFsHandler()
  private val jsonStore = new JsonStoreImpl(fsHandler)

  after {
    fsHandler.clear()
  }

  "JsonStore" should {
    "write correct data" in {
      val tournaments = Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1))
      val requisitions = Set(
        Requisition("test tournament 1", "test agent 1", LocalDateTime.of(2017, 4, 9, 19, 57), 36),
        Requisition("test tournament 2", "test agent 2", LocalDateTime.of(2017, 5, 6, 12, 22), 45)
      )
      val data = Data(tournaments, requisitions)
      val json =
        """{
          |  "requisitions": [{
          |    "agent": "test agent 1",
          |    "dateTime": "2017-04-09T19:57:00",
          |    "questionsCount": 36,
          |    "tournament": "test tournament 1"
          |  }, {
          |    "agent": "test agent 2",
          |    "dateTime": "2017-05-06T12:22:00",
          |    "questionsCount": 45,
          |    "tournament": "test tournament 2"
          |  }],
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
          |  }],
          |  "requisitions": [{
          |    "tournament": "test tournament 1",
          |    "agent": "test agent 1",
          |    "dateTime": "2017-04-09T19:57:00",
          |    "questionsCount": 36
          |  }, {
          |    "tournament": "test tournament 2",
          |    "agent": "test agent 2",
          |    "dateTime": "2017-05-06T12:22:00",
          |    "questionsCount": 45
          |  }]
          |}""".stripMargin, "data.json")
      jsonStore.read shouldEqual Data(
        Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1)),
        Set(
          Requisition("test tournament 1", "test agent 1", LocalDateTime.of(2017, 4, 9, 19, 57), 36),
          Requisition("test tournament 2", "test agent 2", LocalDateTime.of(2017, 5, 6, 12, 22), 45)
        )
      )
    }

    "return empty data when no file" in {
      jsonStore.read shouldEqual Data(Set.empty, Set.empty)
    }

    "return empty data when invalid json in file" in {
      fsHandler.writeFile("test", "data.json")
      jsonStore.read shouldEqual Data(Set.empty, Set.empty)
    }
  }
}
