package com.atanana.parsers

import java.time.LocalDateTime

import com.atanana.data.Requisition
import org.scalatest.{Matchers, WordSpecLike}

import scala.io.Source

class RequisitionsParserTest extends WordSpecLike with Matchers {
  "RequisitionsParser" should {
    "parse valid data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/testValidRequisitions.html", "cp1251").getLines().mkString
      RequisitionsParser().getRequisitionsData(html) shouldEqual List(
        Requisition("Африканский бобр", "Иванов Иван Иванович", LocalDateTime.of(2017, 4, 5, 12, 45)),
        Requisition("Малахитовая шкатулка", "Мерзляков Максим Петрович", LocalDateTime.of(2017, 4, 10, 18, 0))
      )
    }

    "parse empty data" in {
      val html = Source.fromFile("src/test/scala/com/atanana/parsers/emptyRequisitions.html", "cp1251").getLines().mkString
      RequisitionsParser().getRequisitionsData(html) shouldEqual List.empty
    }
  }
}
