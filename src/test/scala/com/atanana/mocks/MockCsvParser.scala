package com.atanana.mocks

import com.atanana.data.TournamentData
import com.atanana.parsers.CsvParser

import scala.collection.mutable

class MockCsvParser extends CsvParser {

  val data: mutable.Map[String, List[TournamentData]] = mutable.Map()

  override def getTournamentsData(csv: String): List[TournamentData] = data(csv)
}
