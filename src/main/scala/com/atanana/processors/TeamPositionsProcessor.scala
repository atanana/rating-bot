package com.atanana.processors

import com.atanana.MessageComposer
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider

import javax.inject.Inject
import scala.concurrent.Future

class TeamPositionsProcessor @Inject()(
                                        infoProvider: TeamPositionsInfoProvider,
                                        messageComposer: MessageComposer,
                                        poster: Poster
                                      ) extends Processor {

  override def process(): Future[Either[String, Unit]] = {
    val result = infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .flatMap(poster.post)
    Future.successful(result)
  }
}