package com.atanana.data

case class TournamentsCheckResult(newTournaments: Set[TournamentData], changedTournaments: Set[ChangedTournament])

case class ChangedTournament(tournament: TournamentData, oldScore: Int)

case class RequisitionsCheckResult(newRequisitions: Set[Requisition], cancelledRequisitions: Set[Requisition])

case class CheckResult(tournamentsCheckResult: TournamentsCheckResult, requisitionsCheckResult: RequisitionsCheckResult)