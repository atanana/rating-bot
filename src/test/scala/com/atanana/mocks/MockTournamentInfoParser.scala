package com.atanana.mocks

import com.atanana.parsers.TournamentInfoParser

import scala.collection.mutable
import scala.util.Try

class MockTournamentInfoParser extends TournamentInfoParser {

  val questionsCount: mutable.Map[String, Try[Int]] = mutable.Map()

  override def getQuestionsCount(tournamentInfo: String): Try[Int] = questionsCount(tournamentInfo)
}
