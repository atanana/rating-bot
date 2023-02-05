package com.atanana.json

import com.atanana.data.Data

trait JsonStore {

  def read: Data

  def write(data: Data): Unit
}
