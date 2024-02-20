package com.atanana.ratingbot.processors

import cats.data.EitherT
import com.atanana.ratingbot.posters.Poster
import com.atanana.ratingbot.providers.{TeamPositionsInfoProvider, TeamPositionsInfoProviderImpl}
import com.atanana.ratingbot.{MessageComposer, MessageComposerImpl}

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