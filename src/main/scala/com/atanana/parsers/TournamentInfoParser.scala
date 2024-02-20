package com.atanana.parsers

import com.atanana.data.TournamentInfo
import com.atanana.types.Pages.TournamentInfoPage

import scala.util.Try

trait TournamentInfoParser {

  def getTournamentInfo(page: TournamentInfoPage): Try[TournamentInfo]
}
