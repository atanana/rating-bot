package com.atanana.ratingbot

import com.atanana.ratingbot.data.*

trait MessageComposer {

  def composeNewResult(data: TournamentResult, info: TournamentInfo): String

  def currentDay(): String

  def composeTeamPositionsMessage(info: TeamPositionsInfo): String

  def composeChangedResult(changedTournament: ChangedTournament, info: TournamentInfo): String
}
