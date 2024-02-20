package com.atanana.ratingbot.processors

import cats.data.EitherT
import com.atanana.ratingbot.mocks.{MockJsonStore, MockMessageComposer, MockPoster}
import com.atanana.ratingbot.MessageComposerImpl
import com.atanana.ratingbot.TestUtils.{getResult, getResultErrorMessage}
import com.atanana.ratingbot.data.{Data, Requisition}
import com.atanana.ratingbot.json.JsonStoreImpl
import com.atanana.ratingbot.posters.Poster
import com.atanana.ratingbot.processors.ReminderProcessorImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global

class ReminderProcessorTest extends AnyWordSpecLike with Matchers {
  private val store = new MockJsonStore()
  private val messageComposer = new MockMessageComposer()
  private val poster = new MockPoster()
  private val processor = new ReminderProcessorImpl(store, messageComposer, poster)

  "ReminderProcessor" should {
    "post correct reminders for tomorrow's requisitions" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(1))
      store.data = Data(Set.empty, Set(requisition))
      messageComposer.requisitionReminderMessages.put(requisition, "reminder")
      poster.responses.put("reminder", EitherT.rightT(()))

      getResult(processor).isRight shouldBe true
      poster.responses shouldBe empty
    }

    "not remind about not tomorrow's requisitions" in {
      store.data = Data(Set.empty, Set(
        Requisition("tournament 1", "agent 1", LocalDateTime.now()),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(2)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().plusMonths(1)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().minusDays(1)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().minusMonths(1))
      ))

      getResult(processor).isRight shouldBe true
    }

    "pass error from poster" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(1))
      store.data = Data(Set.empty, Set(requisition))
      messageComposer.requisitionReminderMessages.put(requisition, "reminder")
      poster.responses.put("reminder", EitherT.leftT(new RuntimeException("123")))

      getResultErrorMessage(processor) shouldEqual "123"
    }
  }
}
