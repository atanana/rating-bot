package com.atanana.processors

import javax.inject.Inject

import com.atanana.providers.TeamPositionsInfoProvider
import com.atanana.{MessageComposer, Poster}

class TeamPositionsProcessor @Inject()(infoProvider: TeamPositionsInfoProvider,
                                       messageComposer: MessageComposer,
                                       poster: Poster) extends Processor {
  override def process(): Unit = {
    infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .map(poster.post)
  }
}
