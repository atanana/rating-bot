package com.atanana.processors

import java.time.LocalDate

import com.atanana.{JsonStore, MessageComposer, Poster}

class ReminderProcessor(store: JsonStore, messageComposer: MessageComposer, poster: Poster) extends Processor {
  override def process(): Unit = {
    val data = store.read
    val tomorrow = LocalDate.now().plusDays(1)
    data.requisitions
      .filter(requisition => requisition.dateTime.toLocalDate == tomorrow)
      .map(messageComposer.composeRequisitionReminder)
      .foreach(poster.post)
  }
}