package com.atanana.ratingbot.net

import com.atanana.ratingbot.types.Ids.{ReleaseId, TeamId, TournamentId}
import sttp.client3.UriContext
import sttp.model.Uri

object UriComposer {
  private val SITE_URL = "https://rating.chgk.info"
  private val API_URL = "https://api.rating.chgk.net"

  def requisitionPageUri(cityId: Int): Uri = uri"$SITE_URL/jq_backend/synch.php?upcoming_synch=true&town_id=$cityId"

  def tournamentPageUri(id: TournamentId): Uri = uri"$SITE_URL/tournament/$id"

  def tournamentRequisitionsPageUri(tournamentId: TournamentId): Uri = uri"$SITE_URL/tournament/$tournamentId/requests"

  def teamsPageUri(releaseId: ReleaseId): Uri = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=&form[releaseId]=$releaseId"

  def cityTeamsPageUri(releaseId: ReleaseId, cityId: Int): Uri = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=$cityId&form[countryId]=&form[releaseId]=$releaseId"

  def countryTeamsPageUri(releaseId: ReleaseId, countryId: Int): Uri = uri"$SITE_URL/ajax/teams/data?draw=&columns[0][data]=rowNumber&columns[1][data]=teamRatingPosition&columns[2][data]=teamRating&columns[3][data]=trb&columns[4][data]=id&columns[5][data]=teamName&columns[6][data]=townName&columns[7][data]=playedTournaments&columns[8][data]=tournamentsPlayedInSeason&columns[9][data]=tournamentsPlayedB&order[0][column]=2&order[0][dir]=desc&start=0&length=500&form[townId]=&form[countryId]=$countryId&form[releaseId]=$releaseId"

  def tournamentInfoUri(tournamentId: TournamentId): Uri = uri"$API_URL/tournaments/$tournamentId"

  def releasesUri: Uri = uri"$API_URL/releases?pagination=false"

  def teamTournamentsUri(teamId: TeamId): Uri = uri"$API_URL/teams/$teamId/tournaments?pagination=false"

  def tournamentResultsUri(id: TournamentId): Uri = uri"$API_URL/tournaments/$id/results?includeRatingB=1"
}
