package com.atanana.processors

import javax.inject.Inject

import com.atanana.MessageComposer
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider

class TeamPositionsProcessor @Inject()(infoProvider: TeamPositionsInfoProvider,
                                       messageComposer: MessageComposer,
                                       poster: Poster) extends Processor {
  override def process(): Unit = {
    infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .map(poster.post)
  }
}
