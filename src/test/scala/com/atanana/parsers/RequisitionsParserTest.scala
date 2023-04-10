package com.atanana.parsers

import com.atanana.data.PartialRequisitionData
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.io.Source
import scala.util.{Failure, Success}

//noinspection SourceNotClosed
class RequisitionsParserTest extends AnyWordSpecLike with Matchers {
  "RequisitionsParser" should {
    "parse valid data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/testValidRequisitions.txt", "cp1251").getLines().mkString
      RequisitionsParserImpl().getRequisitionsData(html) shouldEqual Success(List(
        PartialRequisitionData("Асинхрон «Это не ноготочки»", 9082, "Красковский Дмитрий Дмитриевич", LocalDateTime.of(2023, 4, 10, 20, 0)),
        PartialRequisitionData("Лига Сибири. VI тур (синхрон)", 8308, "Штукатер Дмитрий Сергеевич", LocalDateTime.of(2023, 4, 11, 19, 0))
      ))
    }

    "parse empty data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/emptyRequisitions.txt", "cp1251").getLines().mkString
      RequisitionsParserImpl().getRequisitionsData(html) shouldEqual Success(List.empty)
    }

    "parse incorrect data" in {
      RequisitionsParserImpl().getRequisitionsData("test") shouldEqual Success(List.empty)
    }
  }
}
