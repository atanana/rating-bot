package com.atanana.providers

import com.atanana.data.TournamentData
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TournamentPollingFilterImplTest extends AnyWordSpecLike with Matchers {
  private val filter = new TournamentPollingFilterImpl()

  "TournamentPollingFilter" should {
    "filter out overall standings" in {
      filter.isInteresting("Общий зачёт", TournamentData(1, "test", "", 1.0f, 1, 1)) shouldBe false
    }

    "filter out tournament without results" in {
      filter.isInteresting("Синхрон", TournamentData(1, "test", "", 9999f, 1, 1)) shouldBe false
      filter.isInteresting("Синхрон", TournamentData(1, "test", "", 1.0f, 0, 1)) shouldBe false
    }

    "don't filter out valid tournament" in {
      filter.isInteresting("Синхрон", TournamentData(1, "test", "", 1f, 1, 1)) shouldBe true
    }
  }
}
