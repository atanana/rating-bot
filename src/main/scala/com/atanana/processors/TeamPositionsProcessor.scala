package com.atanana.processors

import cats.data.EitherT
import com.atanana.MessageComposer
import com.atanana.posters.Poster
import com.atanana.providers.TeamPositionsInfoProvider

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamPositionsProcessor @Inject()(
                                        infoProvider: TeamPositionsInfoProvider,
                                        messageComposer: MessageComposer,
                                        poster: Poster
                                      ) extends Processor {

  override def process(): EitherT[Future, Throwable, Unit] = {
    infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .flatMap(poster.postAsync)
  }
}