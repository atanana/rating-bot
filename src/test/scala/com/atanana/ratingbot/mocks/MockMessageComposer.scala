package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.MessageComposer
import com.atanana.ratingbot.data.*

import scala.collection.mutable

class MockMessageComposer extends MessageComposer {

  val newResultMessages: mutable.Map[TournamentResult, String] = mutable.Map()

  val changedResultMessages: mutable.Map[ChangedTournament, String] = mutable.Map()

  val teamPositionsMessage: mutable.Map[TeamPositionsInfo, String] = mutable.Map()

  override def composeNewResult(data: TournamentResult, info: TournamentInfo): String = newResultMessages(data)

  override def currentDay(): String = ???

  override def composeTeamPositionsMessage(info: TeamPositionsInfo): String = teamPositionsMessage(info)

  override def composeChangedResult(changedTournament: ChangedTournament, info: TournamentInfo): String = changedResultMessages(changedTournament)
}
