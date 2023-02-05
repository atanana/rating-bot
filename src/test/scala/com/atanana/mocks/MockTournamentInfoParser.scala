package com.atanana.mocks

import com.atanana.parsers.TournamentInfoParser

import scala.util.Try
import scala.collection.mutable

class MockTournamentInfoParser extends TournamentInfoParser {

  val questionsCount: mutable.Map[String, Try[Int]] = mutable.Map()

  override def getQuestionsCount(tournamentInfo: String): Try[Int] = questionsCount(tournamentInfo)
}
