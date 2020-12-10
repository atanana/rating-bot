package com.atanana.processors

import cats.data.EitherT
import com.atanana.MessageComposer
import com.atanana.json.JsonStore
import com.atanana.posters.Poster
import com.atanana.utils.CollectionsUtils.EitherSet

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReminderProcessor @Inject()(store: JsonStore, messageComposer: MessageComposer, poster: Poster) extends Processor {

  override def process(): EitherT[Future, Throwable, Unit] = {
    val data = store.read
    val tomorrow = LocalDate.now().plusDays(1)
    val messages = data.requisitions
      .filter(requisition => requisition.dateTime.toLocalDate == tomorrow)
      .map(messageComposer.composeRequisitionReminder)
    val result = messages.map(poster.post).unwrap().map(_ => ())
    EitherT(Future.successful(result)).leftMap(new RuntimeException(_))
  }
}
