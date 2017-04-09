package com.atanana

import java.time.LocalDateTime

import com.atanana.data.{Data, Requisition, Tournament}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class JsonStoreTest extends WordSpecLike with BeforeAndAfter with MockFactory with Matchers {
  var fsHandler: FsHandler = _
  var jsonStore: JsonStore = _

  before {
    fsHandler = mock[FsHandler]
    jsonStore = new JsonStore(fsHandler)
  }

  "JsonStore" should {
    "write correct data" in {
      val tournaments = Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1))
      val requisitions = Set(
        Requisition("test tournament 1", "test agent 1", LocalDateTime.of(2017, 4, 9, 19, 57)),
        Requisition("test tournament 2", "test agent 2", LocalDateTime.of(2017, 5, 6, 12, 22))
      )
      val data = Data(tournaments, requisitions)
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
          |  }],
          |  "requisitions": [{
          |    "tournament": "test tournament 1",
          |    "agent": "test agent 1",
          |    "dateTime": "2017-04-09T19:57:00"
          |  }, {
          |    "tournament": "test tournament 2",
          |    "agent": "test agent 2",
          |    "dateTime": "2017-05-06T12:22:00"
          |  }]
          |}""".stripMargin
      (fsHandler.writeFile _).expects(json, JsonStore.FILE_NAME)
      jsonStore.write(data)
    }

    "read data" in {
      (fsHandler.readFile _).expects(JsonStore.FILE_NAME).returns(Success(
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
          |    "dateTime": "2017-04-09T19:57:00"
          |  }, {
          |    "tournament": "test tournament 2",
          |    "agent": "test agent 2",
          |    "dateTime": "2017-05-06T12:22:00"
          |  }]
          |}""".stripMargin))
      jsonStore.read shouldEqual Data(
        Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1)),
        Set(
          Requisition("test tournament 1", "test agent 1", LocalDateTime.of(2017, 4, 9, 19, 57)),
          Requisition("test tournament 2", "test agent 2", LocalDateTime.of(2017, 5, 6, 12, 22))
        )
      )
    }

    "return empty data when no file" in {
      (fsHandler.readFile _).expects(JsonStore.FILE_NAME).returns(Failure(new RuntimeException))
      jsonStore.read shouldEqual Data(Set.empty, Set.empty)
    }

    "return empty data when invalid json in file" in {
      (fsHandler.readFile _).expects(JsonStore.FILE_NAME).returns(Success(""))
      jsonStore.read shouldEqual Data(Set.empty, Set.empty)
    }
  }
}
