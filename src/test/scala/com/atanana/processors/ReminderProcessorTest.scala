package com.atanana.processors

import java.time.LocalDateTime

import com.atanana.MessageComposer
import com.atanana.data.{Data, Requisition}
import com.atanana.json.JsonStore
import com.atanana.posters.Poster
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, WordSpecLike}

class ReminderProcessorTest extends WordSpecLike with MockFactory with BeforeAndAfter {
  var store: JsonStore = _
  var messageComposer: MessageComposer = _
  var poster: Poster = _
  var processor: ReminderProcessor = _

  before {
    store = stub[JsonStore]
    messageComposer = stub[MessageComposer]
    poster = mock[Poster]
    processor = new ReminderProcessor(store, messageComposer, poster)
  }

  "ReminderProcessor" should {
    "post correct reminders for tomorrow's requisitions" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(1))
      (store.read _).when().returns(Data(Set.empty, Set(requisition)))
      (messageComposer.composeRequisitionReminder _).when(requisition).returns("reminder")
      (poster.post _).expects("reminder")

      processor.process()
    }

    "not remind about not tomorrow's requisitions" in {
      (store.read _).when().returns(Data(Set.empty, Set(
        Requisition("tournament 1", "agent 1", LocalDateTime.now()),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(2)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().plusMonths(1)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().minusDays(1)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().minusMonths(1))
      )))

      processor.process()
    }
  }
}
