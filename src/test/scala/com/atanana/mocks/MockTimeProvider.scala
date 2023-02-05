package com.atanana.mocks

import com.atanana.TimeProvider

import java.time.LocalDateTime

class MockTimeProvider extends TimeProvider {

  var dateTime: LocalDateTime = _

  override def now: LocalDateTime = dateTime
}
