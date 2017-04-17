package com.atanana

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class RatingModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[FsHandler]
    bind[JsonStore]
  }
}
