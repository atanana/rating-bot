package com.atanana.data

import java.time.LocalDateTime

import org.scalatest.{Matchers, WordSpecLike}

class RequisitionDataTest extends WordSpecLike with Matchers {
  "RequisitionData" should {
    "correct convert itself" in {
      val time = LocalDateTime.now()
      RequisitionData("tournament 1", 1, "agent 1", time).toRequisition shouldEqual Requisition("tournament 1", "agent 1", time)
    }
  }
}
