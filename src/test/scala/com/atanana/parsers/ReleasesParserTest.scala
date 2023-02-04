package com.atanana.parsers

import com.atanana.data.Release
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.util.Success

class ReleasesParserTest extends AnyWordSpecLike with Matchers {

  val parser = new ReleasesParser()

  "ReleasesParser" should {

    "parse last release id" in {
      val json =
        """[
          |  {
          |    "id": 29,
          |    "date": "2000-12-28T00:00:00+03:00",
          |    "realDate": "2000-12-28T00:00:00+03:00",
          |    "lastRunRefresh": "2018-10-30T02:25:57+03:00"
          |  },
          |  {
          |    "id": 30,
          |    "date": "2000-07-06T00:00:00+04:00",
          |    "realDate": "2000-07-06T00:00:00+04:00",
          |    "lastRunRefresh": "2018-10-30T02:23:08+03:00"
          |  },
          |  {
          |    "id": 31,
          |    "date": "1999-12-30T00:00:00+03:00",
          |    "realDate": "1999-12-30T00:00:00+03:00",
          |    "lastRunRefresh": "2018-10-30T02:21:45+03:00"
          |  },
          |  {
          |    "id": 32,
          |    "date": "2002-06-27T00:00:00+04:00",
          |    "realDate": "2002-06-27T00:00:00+04:00",
          |    "lastRunRefresh": "2018-10-30T02:30:02+03:00"
          |  }
          |]""".stripMargin

      parser.getReleases(json) shouldEqual Success(List(
        Release(29, LocalDateTime.of(2000, 12, 28, 0, 0)),
        Release(30, LocalDateTime.of(2000, 7, 6, 0, 0)),
        Release(31, LocalDateTime.of(1999, 12, 30, 0, 0)),
        Release(32, LocalDateTime.of(2002, 6, 27, 0, 0)),
      ))
    }
  }
}
