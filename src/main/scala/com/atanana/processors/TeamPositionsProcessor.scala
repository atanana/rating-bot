package com.atanana.processors

import com.atanana.MessageComposer
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider

import javax.inject.Inject

class TeamPositionsProcessor @Inject()(
                                        infoProvider: TeamPositionsInfoProvider,
                                        messageComposer: MessageComposer,
                                        poster: Poster
                                      ) extends Processor {

  override def process(): Either[String, Unit] = {
    infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .flatMap(poster.post)
  }
}