package com.atanana.ratingbot

import com.atanana.ratingbot.fs.{FsHandler, FsHandlerImpl}
import com.atanana.ratingbot.json.{JsonConfig, JsonStore, JsonStoreImpl}

object InitModule {

  import com.softwaremill.macwire.*

  lazy val fsHandler: FsHandler = wire[FsHandlerImpl]
  lazy val jsonStore: JsonStore = wire[JsonStoreImpl]
  lazy val jsonConfig: JsonConfig = wire[JsonConfig]
}
