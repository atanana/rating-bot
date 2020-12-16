package com.atanana.checkers

import com.atanana.data.{Requisition, RequisitionData}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.{LocalDateTime, ZoneId}

class RequisitionsCheckerTest extends AnyWordSpecLike with Matchers {
  private val now = LocalDateTime.now(ZoneId.of("Europe/Moscow"))

  "RequisitionsCheckerTest" should {
    "provide correct new requisitions data" in {
      RequisitionsChecker().check(Set(
        Requisition("tournament 1", "agent 1", now.plusDays(1))
      ), Set(
        RequisitionData("tournament 1", 1, "agent 1", now.plusDays(1)),
        RequisitionData("tournament 2", 2, "agent 2", now.plusDays(2)),
        RequisitionData("tournament 3", 3, "agent 3", now.plusDays(3))
      )).newRequisitions shouldEqual Set(
        RequisitionData("tournament 2", 2, "agent 2", now.plusDays(2)),
        RequisitionData("tournament 3", 3, "agent 3", now.plusDays(3))
      )
    }

    "provide correct no new requisitions" in {
      RequisitionsChecker().check(Set(
        Requisition("tournament 1", "agent 1", now.plusDays(1)),
        Requisition("tournament 2", "agent 2", now.plusDays(2)),
        Requisition("tournament 3", "agent 3", now.plusDays(3))
      ), Set(
        RequisitionData("tournament 2", 2, "agent 2", now.plusDays(2)),
        RequisitionData("tournament 3", 3, "agent 3", now.plusDays(3))
      )).newRequisitions shouldEqual Set.empty
    }

    "provide correct cancelled requisitions data" in {
      RequisitionsChecker().check(Set(
        Requisition("tournament 1", "agent 1", now.plusDays(1)),
        Requisition("tournament 2", "agent 2", now.plusDays(2)),
        Requisition("tournament 3", "agent 3", now.plusDays(3))
      ), Set(
        RequisitionData("tournament 1", 1, "agent 1", now.plusDays(1)),
        RequisitionData("tournament 3", 3, "agent 3", now.plusDays(3))
      )).cancelledRequisitions shouldEqual Set(
        Requisition("tournament 2", "agent 2", now.plusDays(2))
      )
    }

    "provide no cancelled requisitions" in {
      RequisitionsChecker().check(Set(
        Requisition("tournament 1", "agent 1", now.plusDays(1)),
        Requisition("tournament 2", "agent 2", now.plusMinutes(15)),
        Requisition("tournament 3", "agent 3", now.plusDays(3))
      ), Set(
        RequisitionData("tournament 1", 1, "agent 1", now.plusDays(1)),
        RequisitionData("tournament 3", 3, "agent 3", now.plusDays(3))
      )).cancelledRequisitions shouldEqual Set.empty
    }
  }
}
