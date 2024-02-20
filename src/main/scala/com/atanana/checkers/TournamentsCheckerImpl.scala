package com.atanana.checkers

import cats.implicits.*
import com.atanana.data.{ChangedTournament, Tournament, TournamentResult, TournamentsCheckResult}

class TournamentsCheckerImpl extends TournamentsChecker {

  override def check(oldTournaments: Set[Tournament], newTournaments: Set[TournamentResult]): TournamentsCheckResult = {
    TournamentsCheckResult(
      getNewTournaments(oldTournaments, newTournaments),
      getChangedTournaments(oldTournaments, newTournaments)
    )
  }

  private def getNewTournaments(oldTournaments: Set[Tournament], newTournaments: Set[TournamentResult]): Set[TournamentResult] = {
    val oldTournamentIds = oldTournaments.map(_.id)
    newTournaments.filter(tournamentResult => !oldTournamentIds.contains(tournamentResult.id))
  }

  private def getChangedTournaments(oldTournaments: Set[Tournament], newTournaments: Set[TournamentResult]): Set[ChangedTournament] = {
    val oldTournamentsMap = oldTournaments.map(tournament => (tournament.id, tournament.score)).toMap
    newTournaments.map { tournament =>
      val oldScore = oldTournamentsMap.getOrElse(tournament.id, tournament.questionsCount)
      (oldScore != tournament.questionsCount).guard[Option].as(ChangedTournament(tournament, oldScore))
    }.flatMap(_.toSet)
  }
}
