package com.atanana

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class ConfigModule(config: Config) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Config].toInstance(config)
    bind[Connector]
    bind[Poster]
    bind[CheckResultHandler]
    bind[Processor]
    bind[TournamentInfoProvider]
  }
}
