package com.atanana.processors

import javax.inject.Inject
import com.atanana.MessageComposer
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider
import com.typesafe.scalalogging.Logger

class TeamPositionsProcessor @Inject()(
                                        infoProvider: TeamPositionsInfoProvider,
                                        messageComposer: MessageComposer,
                                        poster: Poster
                                      ) extends Processor {

  import TeamPositionsProcessor.logger

  override def process(): Unit = {
    val result = infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .map(poster.post)
    result match {
      case Left(errorMessage) => logger.error(errorMessage)
      case Right(_) => // do nothing
    }
  }
}

object TeamPositionsProcessor {
  private val logger = Logger(classOf[TeamPositionsProcessor])
}
