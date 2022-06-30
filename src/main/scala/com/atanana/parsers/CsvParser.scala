package com.atanana.parsers

import com.atanana.Connector
import com.atanana.data.TournamentData
import com.github.tototoshi.csv.CSVReader

import scala.io.Source
import scala.util.Try

class CsvParser {
  def getTournamentsData(csv: String): List[TournamentData] = {
    CSVReader.open(Source.fromString(csv)).all()
      .map(tryParseTournamentRow)
      .flatMap(_.toOption.toList)
      .filter(Function.tupled(isInteresting))
      .map(_._2)
  }

  private def isInteresting(tournamentType: String, data: TournamentData) =
    tournamentType != "Общий зачёт" && data.place != 9999 && data.bonus != 0

  private def tryParseTournamentRow(row: List[String]) = {
    Try {
      val tournamentType = row(3)
      //noinspection ZeroIndexToHead
      val tournamentData = TournamentData(
        row(0).toInt,
        row(1),
        Connector.TOURNAMENT_URL_TEMPLATE + row(0),
        row(8).replace(',', '.').toFloat,
        row(11).toInt,
        row(12).toInt)
      (tournamentType, tournamentData)
    }
  }
}

object CsvParser {
  def apply(): CsvParser = new CsvParser()
}