package com.atanana.checkers

import com.atanana.data.{ChangedTournament, Tournament, TournamentData, TournamentsCheckResult}

class TournamentsCheckerImpl extends TournamentsChecker {
  override def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentData]): TournamentsCheckResult = {
    TournamentsCheckResult(
      getNewTournaments(oldTournaments, newTournaments),
      getChangedTournaments(oldTournaments, newTournaments)
    )
  }

  private def getNewTournaments(oldTournaments: Set[Tournament], newTournaments: Set[TournamentData]): Set[TournamentData] = {
    val newTournamentIds = newTournaments.map(_.id) -- oldTournaments.map(_.id)
    newTournamentIds
      .map(id => newTournaments.find(_.id == id))
      .flatMap(_.toList)
  }

  private def getChangedTournaments(oldTournaments: Set[Tournament], newTournaments: Set[TournamentData]): Set[ChangedTournament] = {
    val changedTournaments = oldTournaments -- newTournaments
    changedTournaments
      .map(oldTournament =>
        newTournaments
          .find(_.id == oldTournament.id)
          .map(ChangedTournament(_, oldTournament.score))
      )
      .flatMap(_.toList)
      .filter(_.tournament.questions > 0) //handle site errors when score actually not changed
  }
}
