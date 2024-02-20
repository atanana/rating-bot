package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.data.TournamentInfo
import com.atanana.ratingbot.parsers.TournamentInfoParser
import com.atanana.ratingbot.types.Pages.TournamentInfoPage

import scala.collection.mutable
import scala.util.Try

class MockTournamentInfoParser extends TournamentInfoParser {

  val results: mutable.Map[TournamentInfoPage, Try[TournamentInfo]] = mutable.Map()

  override def getTournamentInfo(page: TournamentInfoPage): Try[TournamentInfo] = results(page)
}
