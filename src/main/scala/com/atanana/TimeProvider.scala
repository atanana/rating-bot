package com.atanana

import java.time.LocalDateTime

trait TimeProvider {

  def now: LocalDateTime
}
