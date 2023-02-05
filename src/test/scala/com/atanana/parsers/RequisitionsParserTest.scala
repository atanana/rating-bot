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
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/testValidRequisitions.html", "cp1251").getLines().mkString
      RequisitionsParserImpl().getRequisitionsData(html) shouldEqual Success(List(
        PartialRequisitionData("Африканский бобр", 422, "Иванов Иван Иванович", LocalDateTime.of(2017, 4, 5, 12, 45)),
        PartialRequisitionData("Малахитовая шкатулка", 4220, "Мерзляков Максим Петрович", LocalDateTime.of(2017, 4, 10, 18, 0))
      ))
    }

    "parse empty data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/emptyRequisitions.html", "cp1251").getLines().mkString
      RequisitionsParserImpl().getRequisitionsData(html) shouldEqual Success(List.empty)
    }

    "parse incorrect data" in {
      RequisitionsParserImpl().getRequisitionsData("html") shouldBe a[Failure[_]]
    }
  }
}
