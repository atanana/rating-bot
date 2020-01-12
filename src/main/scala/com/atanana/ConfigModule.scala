package com.atanana

import com.atanana.json.Config
import com.atanana.posters.{Poster, RealPoster, TestPoster}
import com.atanana.processors.{CommandProcessor, PollProcessor, ReminderProcessor, TeamPositionsProcessor}
import com.atanana.providers.{PollingDataProvider, TeamPositionsInfoComposer, TeamPositionsInfoProvider, TournamentInfoProvider}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class ConfigModule(config: Config, isDebug: Boolean) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Config].toInstance(config)
    if (isDebug) {
      bind[Poster].to[TestPoster]
    } else {
      bind[Poster].to[RealPoster]
    }
    bind[CheckResultHandler]
    bind[TournamentInfoProvider]
    bind[PollingDataProvider]

    bind[NetWrapper]
    bind[Connector]

    bind[TeamPositionsInfoProvider]
    bind[TeamPositionsInfoComposer].toInstance(new TeamPositionsInfoComposer(config.team))

    bind[CommandProcessor]
    bind[PollProcessor]
    bind[ReminderProcessor]
    bind[TeamPositionsProcessor]
  }
}
