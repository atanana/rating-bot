package com.atanana.mocks

import com.atanana.data.Data
import com.atanana.json.JsonStore

class MockJsonStore extends JsonStore {

  var data: Data = _

  override def read: Data = data

  override def write(data: Data): Unit = ???
}
