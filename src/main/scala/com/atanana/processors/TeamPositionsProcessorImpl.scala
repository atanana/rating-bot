package com.atanana.processors

import cats.data.EitherT
import com.atanana.{MessageComposer, MessageComposerImpl}
import com.atanana.posters.Poster
import com.atanana.providers.{TeamPositionsInfoProvider, TeamPositionsInfoProviderImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamPositionsProcessorImpl(
                                  infoProvider: TeamPositionsInfoProvider,
                                  messageComposer: MessageComposer,
                                  poster: Poster
                                ) extends TeamPositionsProcessor {

  override def process(): EitherT[Future, Throwable, Unit] = {
    infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .flatMap(poster.postAsync)
  }
}