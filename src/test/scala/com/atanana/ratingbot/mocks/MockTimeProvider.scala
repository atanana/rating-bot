package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.TimeProvider

import java.time.LocalDateTime

class MockTimeProvider extends TimeProvider {

  var dateTime: LocalDateTime = _

  override def now: LocalDateTime = dateTime
}
