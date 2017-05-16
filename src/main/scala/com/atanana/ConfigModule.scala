package com.atanana

import com.atanana.processors.{CommandProcessor, PollProcessor, ReminderProcessor, TeamPositionsProcessor}
import com.atanana.providers.{PollingDataProvider, TournamentInfoProvider}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class ConfigModule(config: Config) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Config].toInstance(config)
    bind[Poster]
    bind[CheckResultHandler]
    bind[TournamentInfoProvider]
    bind[PollingDataProvider]

    bind[NetWrapper]
    bind[Connector]

    bind[CommandProcessor]
    bind[PollProcessor]
    bind[ReminderProcessor]
    bind[TeamPositionsProcessor]
  }
}
