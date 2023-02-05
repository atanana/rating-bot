package com.atanana

import java.time.LocalDateTime

class TimeProviderImpl extends TimeProvider {

  override def now: LocalDateTime = LocalDateTime.now()
}
