package com.atanana.ratingbot.data

case class TournamentsCheckResult(newTournaments: Set[TournamentResult], changedTournaments: Set[ChangedTournament])

case class ChangedTournament(tournament: TournamentResult, oldScore: Int)

case class CheckResult(tournamentsCheckResult: TournamentsCheckResult)