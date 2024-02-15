package com.atanana.data

import com.atanana.types.Ids.TournamentId
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime

class RequisitionDataTest extends AnyWordSpecLike with Matchers {
  "RequisitionData" should {
    "correct convert itself" in {
      val time = LocalDateTime.now()
      RequisitionData("tournament 1", TournamentId(1), "agent 1", time).toRequisition shouldEqual Requisition("tournament 1", "agent 1", time)
    }
  }
}
