package com.atanana

import com.atanana.data.{Data, Tournament}
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
      val data = Data(Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1)))
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
          |  }]
          |}""".stripMargin))
      jsonStore.read shouldEqual Data(Set(Tournament(1, 3), Tournament(2, 2), Tournament(3, 1)))
    }

    "return empty data when no file" in {
      (fsHandler.readFile _).expects(JsonStore.FILE_NAME).returns(Failure(new RuntimeException))
      jsonStore.read shouldEqual Data(Set.empty)
    }

    "return empty data when invalid json in file" in {
      (fsHandler.readFile _).expects(JsonStore.FILE_NAME).returns(Success(""))
      jsonStore.read shouldEqual Data(Set.empty)
    }
  }
}
