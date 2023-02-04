package com.atanana

import com.atanana.json.{JsonConfig, JsonStore}

trait InitModule {

  import com.softwaremill.macwire._

  lazy val fsHandler: FsHandler = wire[FsHandler]
  lazy val jsonStore: JsonStore = wire[JsonStore]
  lazy val jsonConfig: JsonConfig = wire[JsonConfig]
}
