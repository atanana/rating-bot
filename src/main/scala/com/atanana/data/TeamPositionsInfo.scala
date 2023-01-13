package com.atanana.data

case class TeamPositionsInfo(
                              targetAllRatingTeam: Option[TargetTeam],
                              targetCountryRatingTeam: Option[TargetTeam],
                              overtakingCountryRatingTeam: TargetTeam,
                              top100ratingDifference: Int,
                              currentPosition: Int,
                              currentRating: Int,
                              cityPosition: Int,
                              countryPosition: Int
                            )

case class TargetTeam(name: String, city: String, ratingDifference: Int)

object TargetTeam {
  def apply(team: Team, teamToCompare: Team): TargetTeam =
    new TargetTeam(team.name, team.city, team.rating - teamToCompare.rating)
}