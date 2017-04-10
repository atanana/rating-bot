package com.atanana.checkers

import com.atanana.data.{Tournament, TournamentData}

class TournamentsChecker {
  def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentData]): TournamentsCheckResult = {
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
    val newTournamentScores = newTournaments.map(tournament => Tournament(tournament.id, tournament.questions))
    val changedTournaments = oldTournaments -- newTournamentScores
    changedTournaments
      .map(oldTournament =>
        newTournaments
          .find(_.id == oldTournament.id)
          .map(ChangedTournament(_, oldTournament.score))
      )
      .flatMap(_.toList)
  }
}

case class TournamentsCheckResult(newTournaments: Set[TournamentData], changedTournaments: Set[ChangedTournament])

case class ChangedTournament(tournament: TournamentData, oldScore: Int)

object TournamentsChecker {
  def apply(): TournamentsChecker = new TournamentsChecker()
}