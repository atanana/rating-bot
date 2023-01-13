package com.atanana.providers

import com.atanana.data.{TargetTeam, Team, TeamPositionsInfo}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TeamPositionsInfoComposerTest extends AnyWordSpecLike with Matchers {
  val teamId = 123
  val provider = new TeamPositionsInfoComposer(teamId)

  "TeamPositionsInfoComposer" should {
    "provide correct positions info" in {
      val targetAllRatingTeamName = "test all team"
      val targetAllRatingTeamCity = "test all city"
      val targetCountryRatingTeamName = "test country team"
      val targetCountryRatingTeamCity = "test country city"
      val overcomingRatingTeamName = "test overcoming team"
      val overcomingRatingTeamCity = "test overcoming city"
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(200, Team(teamId, "team", "city", 49, 200f))
        .updated(199, Team(111, targetAllRatingTeamName, targetAllRatingTeamCity, 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(19, Team(teamId, "team", "city", 49, 20f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(29, Team(teamId, "team", "city", 49, 30f))
        .updated(28, Team(1, targetCountryRatingTeamName, targetCountryRatingTeamCity, 50, 30f))
        .updated(30, Team(1, overcomingRatingTeamName, overcomingRatingTeamCity, 47, 30f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Right(TeamPositionsInfo(
        Some(TargetTeam(targetAllRatingTeamName, targetAllRatingTeamCity, 2)),
        Some(TargetTeam(targetCountryRatingTeamName, targetCountryRatingTeamCity, 1)),
        TargetTeam(overcomingRatingTeamName, overcomingRatingTeamCity, -2),
        51,
        200f,
        49,
        20,
        30
      ))
    }

    "provide correct positions info when team on the first place" in {
      val overcomingRatingTeamName = "test overcoming team"
      val overcomingRatingTeamCity = "test overcoming city"
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(0, Team(teamId, "team", "city", 49, 1f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(0, Team(teamId, "team", "city", 49, 1f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(0, Team(teamId, "team", "city", 49, 1f))
        .updated(1, Team(1, overcomingRatingTeamName, overcomingRatingTeamCity, 47, 2f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Right(TeamPositionsInfo(
        None,
        None,
        TargetTeam(overcomingRatingTeamName, overcomingRatingTeamCity, -2),
        51,
        1f,
        49,
        1,
        1
      ))
    }

    "fails when no team in top 500" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(199, Team(111, "target team", "target city", 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(19, Team(teamId, "team", "city", 49, 20f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(29, Team(teamId, "team", "city", 49, 30f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Left("No target team in top 500!")
    }

    "fails when no team in city teams" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(200, Team(teamId, "team", "city", 49, 200f))
        .updated(199, Team(111, "target team", "target city", 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(29, Team(teamId, "team", "city", 49, 30f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Left("No target team in city teams!")
    }

    "fails when no team in country teams" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(200, Team(teamId, "team", "city", 49, 200f))
        .updated(199, Team(111, "target team", "target city", 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(19, Team(teamId, "team", "city", 49, 20f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Left("No target team in country teams!")
    }
  }
}
