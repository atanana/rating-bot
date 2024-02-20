package com.atanana.ratingbot

import com.atanana.ratingbot.TimeProvider

import java.time.LocalDateTime

class TimeProviderImpl extends TimeProvider {

  override def now: LocalDateTime = LocalDateTime.now()
}
