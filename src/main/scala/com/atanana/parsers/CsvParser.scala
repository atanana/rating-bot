package com.atanana.parsers

import com.atanana.data.TournamentData
import com.atanana.net.ConnectorImpl
import com.atanana.providers.TournamentPollingFilter
import com.github.tototoshi.csv.CSVReader

import scala.io.Source
import scala.util.Try

class CsvParser(filter: TournamentPollingFilter) {
  def getTournamentsData(csv: String): List[TournamentData] = {
    CSVReader.open(Source.fromString(csv)).all()
      .map(tryParseTournamentRow)
      .flatMap(_.toOption.toList)
      .filter(Function.tupled(filter.isInteresting))
      .map(_._2)
  }

  private def tryParseTournamentRow(row: List[String]) = {
    Try {
      val tournamentType = row(3)
      //noinspection ZeroIndexToHead
      val tournamentData = TournamentData(
        row(0).toInt,
        row(1),
        ConnectorImpl.TOURNAMENT_URL_TEMPLATE + row(0),
        row(8).replace(',', '.').toFloat,
        row(11).toInt,
        row(12).toInt)
      (tournamentType, tournamentData)
    }
  }
}