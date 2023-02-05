package com.atanana.providers

import com.atanana.data.TournamentData

trait TournamentPollingFilter {

  def isInteresting(tournamentType: String, data: TournamentData): Boolean
}
