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
        """
          |{
          |  "@context": "\/contexts\/Release",
          |  "@id": "\/releases",
          |  "@type": "hydra:Collection",
          |  "hydra:member": [
          |    {
          |      "@id": "\/releases\/1593",
          |      "@type": "Release",
          |      "id": 1593,
          |      "date": "2022-12-22T00:00:00+03:00",
          |      "realDate": "2022-12-22T00:00:00+03:00",
          |      "lastRunRefresh": "2023-01-08T02:00:02+03:00"
          |    },
          |    {
          |      "@id": "\/releases\/1594",
          |      "@type": "Release",
          |      "id": 1594,
          |      "date": "2022-12-29T00:00:00+03:00",
          |      "realDate": "2022-12-29T00:00:00+03:00",
          |      "lastRunRefresh": "2023-01-08T02:00:02+03:00"
          |    },
          |    {
          |      "@id": "\/releases\/1595",
          |      "@type": "Release",
          |      "id": 1595,
          |      "date": "2023-01-05T00:00:00+03:00",
          |      "realDate": "2023-01-05T00:00:00+03:00",
          |      "lastRunRefresh": "2023-01-08T02:00:02+03:00"
          |    },
          |    {
          |      "@id": "\/releases\/1596",
          |      "@type": "Release",
          |      "id": 1596,
          |      "date": "2023-01-12T00:00:00+03:00",
          |      "realDate": "2023-01-12T00:00:00+03:00",
          |      "lastRunRefresh": "2023-01-08T02:00:02+03:00"
          |    }
          |  ],
          |  "hydra:totalItems": 1168
          |}
          |""".stripMargin

      parser.getReleases(json) shouldEqual Success(List(
        Release(1593, LocalDateTime.of(2022, 12, 22, 0, 0)),
        Release(1594, LocalDateTime.of(2022, 12, 29, 0, 0)),
        Release(1595, LocalDateTime.of(2023, 1, 5, 0, 0)),
        Release(1596, LocalDateTime.of(2023, 1, 12, 0, 0)),
      ))
    }
  }
}
