package com.atanana.parsers

import scala.util.Try

trait TournamentInfoParser {

  def getQuestionsCount(tournamentInfo: String): Try[Int]
}
