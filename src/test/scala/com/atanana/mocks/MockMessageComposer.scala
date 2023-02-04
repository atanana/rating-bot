package com.atanana.mocks

import com.atanana.MessageComposer
import com.atanana.data.{ChangedTournament, Editor, Requisition, TeamPositionsInfo, TournamentData}

import scala.collection.mutable

class MockMessageComposer extends MessageComposer {

  val newResultMessages: mutable.Map[TournamentData, String] = mutable.Map[TournamentData, String]()

  val changedResultMessages: mutable.Map[ChangedTournament, String] = mutable.Map[ChangedTournament, String]()

  val newRequisitionMessages: mutable.Map[(Requisition, List[Editor]), String] = mutable.Map[(Requisition, List[Editor]), String]()

  val cancelledRequisitionMessages: mutable.Map[Requisition, String] = mutable.Map[Requisition, String]()

  override def composeNewResult(data: TournamentData): String = newResultMessages(data)

  override def currentDay(): String = ???

  override def composeNewRequisition(requisition: Requisition, editors: List[Editor]): String =
    newRequisitionMessages((requisition, editors))

  override def composeRequisitionReminder(requisition: Requisition): String = ???

  override def composeCancelledRequisition(requisition: Requisition): String = cancelledRequisitionMessages(requisition)

  override def composeTeamPositionsMessage(info: TeamPositionsInfo): String = ???

  override def composeChangedResult(changedTournament: ChangedTournament): String = changedResultMessages(changedTournament)
}
