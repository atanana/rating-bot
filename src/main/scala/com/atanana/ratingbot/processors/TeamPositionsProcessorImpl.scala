package com.atanana.ratingbot.processors

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.posters.Poster
import com.atanana.ratingbot.providers.{TeamPositionsInfoProvider, TeamPositionsInfoProviderImpl}
import com.atanana.ratingbot.{MessageComposer, MessageComposerImpl}

class TeamPositionsProcessorImpl(
                                  infoProvider: TeamPositionsInfoProvider,
                                  messageComposer: MessageComposer,
                                  poster: Poster
                                ) extends TeamPositionsProcessor {

  override def process(): EitherT[IO, Throwable, Unit] = {
    infoProvider.data
      .map(messageComposer.composeTeamPositionsMessage)
      .flatMap(poster.postAsync)
  }
}