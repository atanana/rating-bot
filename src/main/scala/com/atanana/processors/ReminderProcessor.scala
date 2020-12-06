package com.atanana.processors

import com.atanana.MessageComposer
import com.atanana.json.JsonStore
import com.atanana.posters.Poster
import com.atanana.utils.CollectionsUtils.EitherSet

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.Future

class ReminderProcessor @Inject()(store: JsonStore, messageComposer: MessageComposer, poster: Poster) extends Processor {

  override def process(): Future[Either[String, Unit]] = {
    val data = store.read
    val tomorrow = LocalDate.now().plusDays(1)
    val messages = data.requisitions
      .filter(requisition => requisition.dateTime.toLocalDate == tomorrow)
      .map(messageComposer.composeRequisitionReminder)
    val result = messages.map(poster.post).unwrap().map(_ => ())
    Future.successful(result)
  }
}
