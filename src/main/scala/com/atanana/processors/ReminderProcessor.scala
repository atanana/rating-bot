package com.atanana.processors

import java.time.LocalDate
import javax.inject.Inject

import com.atanana.json.JsonStore
import com.atanana.{MessageComposer, Poster}

class ReminderProcessor @Inject()(store: JsonStore, messageComposer: MessageComposer, poster: Poster) extends Processor {
  override def process(): Unit = {
    val data = store.read
    val tomorrow = LocalDate.now().plusDays(1)
    data.requisitions
      .filter(requisition => requisition.dateTime.toLocalDate == tomorrow)
      .map(messageComposer.composeRequisitionReminder)
      .foreach(poster.post)
  }
}
