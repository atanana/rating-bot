package com.atanana.parsers

import com.atanana.data.TournamentData

trait CsvParser {

  def getTournamentsData(csv: String): List[TournamentData]
}
