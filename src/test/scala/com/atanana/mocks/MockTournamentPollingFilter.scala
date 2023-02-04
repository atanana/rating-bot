package com.atanana.mocks

import com.atanana.data.TournamentData
import com.atanana.providers.TournamentPollingFilter

class MockTournamentPollingFilter extends TournamentPollingFilter {

  var isInteresting = false

  override def isInteresting(tournamentType: String, data: TournamentData): Boolean = isInteresting
}
