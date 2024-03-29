package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.MessageComposer
import com.atanana.ratingbot.data.*

import scala.collection.mutable

class MockMessageComposer extends MessageComposer {

  val newResultMessages: mutable.Map[TournamentResult, String] = mutable.Map()

  val changedResultMessages: mutable.Map[ChangedTournament, String] = mutable.Map()

  val newRequisitionMessages: mutable.Map[(Requisition, List[Editor]), String] = mutable.Map()

  val cancelledRequisitionMessages: mutable.Map[Requisition, String] = mutable.Map()

  val teamPositionsMessage: mutable.Map[TeamPositionsInfo, String] = mutable.Map()

  val requisitionReminderMessages: mutable.Map[Requisition, String] = mutable.Map()

  override def composeNewResult(data: TournamentResult, info: TournamentInfo): String = newResultMessages(data)

  override def currentDay(): String = ???

  override def composeNewRequisition(requisition: Requisition, editors: List[Editor]): String =
    newRequisitionMessages((requisition, editors))

  override def composeRequisitionReminder(requisition: Requisition): String = requisitionReminderMessages(requisition)

  override def composeCancelledRequisition(requisition: Requisition): String = cancelledRequisitionMessages(requisition)

  override def composeTeamPositionsMessage(info: TeamPositionsInfo): String = teamPositionsMessage(info)

  override def composeChangedResult(changedTournament: ChangedTournament, info: TournamentInfo): String = changedResultMessages(changedTournament)
}
