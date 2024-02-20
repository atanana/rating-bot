package com.atanana.ratingbot

import java.time.LocalDateTime

trait TimeProvider {

  def now: LocalDateTime
}
