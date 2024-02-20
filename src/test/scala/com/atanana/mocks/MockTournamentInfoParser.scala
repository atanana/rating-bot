package com.atanana.mocks

import com.atanana.data.TournamentInfo
import com.atanana.parsers.TournamentInfoParser
import com.atanana.types.Pages.TournamentInfoPage

import scala.collection.mutable
import scala.util.Try

class MockTournamentInfoParser extends TournamentInfoParser {

  val results: mutable.Map[TournamentInfoPage, Try[TournamentInfo]] = mutable.Map()

  override def getTournamentInfo(page: TournamentInfoPage): Try[TournamentInfo] = results(page)
}
