package com.atanana.data

case class TournamentsCheckResult(newTournaments: Set[TournamentResult], changedTournaments: Set[ChangedTournament])

case class ChangedTournament(tournament: TournamentResult, oldScore: Int)

case class RequisitionsCheckResult(newRequisitions: Set[RequisitionData], cancelledRequisitions: Set[Requisition])

case class CheckResult(tournamentsCheckResult: TournamentsCheckResult, requisitionsCheckResult: RequisitionsCheckResult)