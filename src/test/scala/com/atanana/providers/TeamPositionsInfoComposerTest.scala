package com.atanana.providers

import com.atanana.data.{TargetTeam, Team, TeamPositionsInfo}
import org.scalatest.{Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class TeamPositionsInfoComposerTest extends WordSpecLike with Matchers {
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
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Success(TeamPositionsInfo(
        TargetTeam(targetAllRatingTeamName, targetAllRatingTeamCity, 2),
        TargetTeam(targetCountryRatingTeamName, targetCountryRatingTeamCity, 1),
        TargetTeam(overcomingRatingTeamName, overcomingRatingTeamCity, -2),
        51,
        200f,
        49,
        20,
        30
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
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldBe a[Failure[_]]
    }

    "fails when no team in city teams" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(200, Team(teamId, "team", "city", 49, 200f))
        .updated(199, Team(111, "target team", "target city", 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(29, Team(teamId, "team", "city", 49, 30f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldBe a[Failure[_]]
    }

    "fails when no team in country teams" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(200, Team(teamId, "team", "city", 49, 200f))
        .updated(199, Team(111, "target team", "target city", 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(19, Team(teamId, "team", "city", 49, 20f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldBe a[Failure[_]]
    }
  }
}
