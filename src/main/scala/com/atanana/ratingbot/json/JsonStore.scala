package com.atanana.ratingbot.json

import com.atanana.ratingbot.data.Data

trait JsonStore {

  def read: Data

  def write(data: Data): Unit
}
