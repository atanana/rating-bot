package com.atanana.ratingbot.processors

import cats.data.EitherT
import cats.implicits.toTraverseOps
import com.atanana.ratingbot.json.{JsonStore, JsonStoreImpl}
import com.atanana.ratingbot.posters.Poster
import com.atanana.ratingbot.{MessageComposer, MessageComposerImpl}

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReminderProcessorImpl(store: JsonStore, messageComposer: MessageComposer, poster: Poster) extends ReminderProcessor {

  override def process(): EitherT[Future, Throwable, Unit] = {
    val data = store.read
    val tomorrow = LocalDate.now().plusDays(1)
    val messages = data.requisitions
      .filter(requisition => requisition.dateTime.toLocalDate == tomorrow)
      .map(messageComposer.composeRequisitionReminder)
    messages.map(poster.postAsync).toList.sequence.map(_ => ())
  }
}
