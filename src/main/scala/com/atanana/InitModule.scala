package com.atanana

import com.atanana.fs.{FsHandler, FsHandlerImpl}
import com.atanana.json.{JsonConfig, JsonStore, JsonStoreImpl}

object InitModule {

  import com.softwaremill.macwire._

  lazy val fsHandler: FsHandler = wire[FsHandlerImpl]
  lazy val jsonStore: JsonStore = wire[JsonStoreImpl]
  lazy val jsonConfig: JsonConfig = wire[JsonConfig]
}
