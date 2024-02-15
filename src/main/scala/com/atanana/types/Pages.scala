package com.atanana.types

import spray.json.*

object Pages {

  opaque type TeamTournamentsPage = String

  object TeamTournamentsPage {

    def apply(string: String): TeamTournamentsPage = string

    extension (page: TeamTournamentsPage) def toJson: JsValue = page.parseJson
  }

  opaque type TournamentResultsPage = String

  object TournamentResultsPage {

    def apply(string: String): TournamentResultsPage = string

    extension (page: TournamentResultsPage) def toJson: JsValue = page.parseJson
  }
}