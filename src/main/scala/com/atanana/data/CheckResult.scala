package com.atanana.data

case class TournamentsCheckResult(newTournaments: Set[TournamentData], changedTournaments: Set[ChangedTournament])

case class ChangedTournament(tournament: TournamentData, oldScore: Int)

case class RequisitionsCheckResult(newRequisitions: Set[RequisitionData], cancelledRequisitions: Set[Requisition])

case class CheckResult(tournamentsCheckResult: TournamentsCheckResult, requisitionsCheckResult: RequisitionsCheckResult)

object RequisitionsCheckResult {
  val EMPTY = RequisitionsCheckResult(Set.empty, Set.empty)
}