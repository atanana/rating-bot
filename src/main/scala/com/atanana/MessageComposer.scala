package com.atanana

import com.atanana.data.{ChangedTournament, Editor, Requisition, TeamPositionsInfo, TournamentData}

trait MessageComposer {

  def composeNewResult(data: TournamentData): String

  def currentDay(): String

  def composeNewRequisition(requisition: Requisition, editors: List[Editor]): String

  def composeRequisitionReminder(requisition: Requisition): String

  def composeCancelledRequisition(requisition: Requisition): String

  def composeTeamPositionsMessage(info: TeamPositionsInfo): String

  def composeChangedResult(changedTournament: ChangedTournament): String
}
