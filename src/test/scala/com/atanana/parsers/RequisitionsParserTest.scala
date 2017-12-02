package com.atanana.parsers

import java.time.LocalDateTime

import com.atanana.data.RequisitionData
import org.scalatest.{Matchers, WordSpecLike}

import scala.io.Source
import scala.util.Success

class RequisitionsParserTest extends WordSpecLike with Matchers {
  "RequisitionsParser" should {
    "parse valid data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/testValidRequisitions.html", "cp1251").getLines().mkString
      RequisitionsParser().getRequisitionsData(html) shouldEqual Success(List(
        RequisitionData("Африканский бобр", 422, "Иванов Иван Иванович", LocalDateTime.of(2017, 4, 5, 12, 45)),
        RequisitionData("Малахитовая шкатулка", 4220, "Мерзляков Максим Петрович", LocalDateTime.of(2017, 4, 10, 18, 0))
      ))
    }

    "parse empty data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/emptyRequisitions.html", "cp1251").getLines().mkString
      RequisitionsParser().getRequisitionsData(html) shouldEqual Success(List.empty)
    }
  }
}
