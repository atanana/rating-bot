package com.atanana

import com.atanana.data.TournamentData

import scala.util.Try

class CsvParser {
  def getTournamentsData(csv: String): List[TournamentData] = {
    csv.split("\r\n").toList match {
      case _ :: data =>
        data
          .map(_.split(';'))
          .map(tryParseTournamentRow)
          .flatMap(_.toOption.toList)
          .filter(_.place != 9999)
      case _ => List.empty
    }
  }

  private def tryParseTournamentRow(row: Array[String]) = {
    Try({
      TournamentData(
        row(0).toInt,
        row(1),
        Connector.TOURNAMENT_URL_TEMPLATE + row(0),
        row(8).replace(',', '.').toFloat,
        row(11).toInt,
        row(12).toInt)
    })
  }
}

object CsvParser {
  def apply(): CsvParser = new CsvParser()
}