package com.atanana.processors

import com.atanana.MessageComposer
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider
import com.typesafe.scalalogging.Logger

import javax.inject.Inject

class TeamPositionsProcessor @Inject()(
                                        infoProvider: TeamPositionsInfoProvider,
                                        messageComposer: MessageComposer,
                                        poster: Poster
                                      ) extends Processor {

  import TeamPositionsProcessor.logger

  override def process(): Either[String, Unit] = {
    val result = infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .map(poster.post)
    result match {
      case Left(errorMessage) => logger.error(errorMessage)
      case Right(_) => // do nothing
    }
    Right()
  }
}

object TeamPositionsProcessor {
  private val logger = Logger(classOf[TeamPositionsProcessor])
}
