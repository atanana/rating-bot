package com.atanana.processors

import cats.data.EitherT
import cats.implicits.toTraverseOps
import com.atanana.MessageComposer
import com.atanana.json.JsonStore
import com.atanana.posters.Poster

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReminderProcessor(store: JsonStore, messageComposer: MessageComposer, poster: Poster) extends Processor {

  override def process(): EitherT[Future, Throwable, Unit] = {
    val data = store.read
    val tomorrow = LocalDate.now().plusDays(1)
    val messages = data.requisitions
      .filter(requisition => requisition.dateTime.toLocalDate == tomorrow)
      .map(messageComposer.composeRequisitionReminder)
    messages.map(poster.postAsync).toList.sequence.map(_ => ())
  }
}
