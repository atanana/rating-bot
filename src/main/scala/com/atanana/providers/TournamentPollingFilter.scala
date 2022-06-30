package com.atanana.providers

import com.atanana.data.TournamentData

class TournamentPollingFilter {
  def isInteresting(tournamentType: String, data: TournamentData): Boolean =
    tournamentType != "Общий зачёт" && data.place != 9999 && data.bonus != 0
}
