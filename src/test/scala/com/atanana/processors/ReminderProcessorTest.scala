package com.atanana.processors

import cats.data.EitherT
import com.atanana.MessageComposer
import com.atanana.TestUtils.{getResult, getResultErrorMessage}
import com.atanana.data.{Data, Requisition}
import com.atanana.json.JsonStore
import com.atanana.posters.Poster
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global

class ReminderProcessorTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val store = stub[JsonStore]
  private val messageComposer = stub[MessageComposer]
  private val poster = mock[Poster]
  private val processor = new ReminderProcessor(store, messageComposer, poster)

  "ReminderProcessor" should {
    "post correct reminders for tomorrow's requisitions" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(1))
      (store.read _).when().returns(Data(Set.empty, Set(requisition)))
      (messageComposer.composeRequisitionReminder _).when(requisition).returns("reminder")
      (poster.postAsync _).expects("reminder") returns EitherT.rightT(())

      getResult(processor) shouldEqual Right()
    }

    "not remind about not tomorrow's requisitions" in {
      (store.read _).when().returns(Data(Set.empty, Set(
        Requisition("tournament 1", "agent 1", LocalDateTime.now()),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(2)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().plusMonths(1)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().minusDays(1)),
        Requisition("tournament 1", "agent 1", LocalDateTime.now().minusMonths(1))
      )))

      getResult(processor) shouldEqual Right()
    }

    "pass error from poster" in {
      val requisition = Requisition("tournament 1", "agent 1", LocalDateTime.now().plusDays(1))
      (store.read _).when().returns(Data(Set.empty, Set(requisition)))
      (messageComposer.composeRequisitionReminder _).when(requisition).returns("reminder")
      (poster.postAsync _).expects("reminder") returns EitherT.leftT(new RuntimeException("123"))

      getResultErrorMessage(processor) shouldEqual "123"
    }
  }
}
