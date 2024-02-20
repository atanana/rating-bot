package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.TournamentInfo
import com.atanana.ratingbot.types.Pages.TournamentInfoPage

import scala.util.Try

trait TournamentInfoParser {

  def getTournamentInfo(page: TournamentInfoPage): Try[TournamentInfo]
}
