package com.atanana.ratingbot.providers

import cats.implicits.*
import com.atanana.ratingbot.data.{TargetTeam, Team, TeamPositionsInfo}
import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.providers.TeamPositionsInfoComposerImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TeamPositionsInfoComposerTest extends AnyWordSpecLike with Matchers {
  val teamId = 123
  val provider = new TeamPositionsInfoComposerImpl(Config("", "", 0, teamId, 0, "pipe", 0, List.empty))

  "TeamPositionsInfoComposer" should {
    "provide correct positions info" in {
      val targetAllRatingTeamName = "test all team"
      val targetAllRatingTeamCity = "test all city"
      val targetCountryRatingTeamName = "test country team"
      val targetCountryRatingTeamCity = "test country city"
      val overcomingRatingTeamName = "test overcoming team"
      val overcomingRatingTeamCity = "test overcoming city"
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1))
        .updated(99, Team(321, "last team", "last city", 100, 100))
        .updated(200, Team(teamId, "team", "city", 49, 200))
        .updated(199, Team(111, targetAllRatingTeamName, targetAllRatingTeamCity, 51, 199))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(19, Team(teamId, "team", "city", 49, 20))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(29, Team(teamId, "team", "city", 49, 30))
        .updated(28, Team(1, targetCountryRatingTeamName, targetCountryRatingTeamCity, 50, 30))
        .updated(30, Team(1, overcomingRatingTeamName, overcomingRatingTeamCity, 47, 30))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Right(TeamPositionsInfo(
        TargetTeam(targetAllRatingTeamName, targetAllRatingTeamCity, 2).some,
        TargetTeam(targetCountryRatingTeamName, targetCountryRatingTeamCity, 1).some,
        TargetTeam(overcomingRatingTeamName, overcomingRatingTeamCity, -2),
        51,
        200,
        49,
        20,
        30
      ))
    }

    "provide correct positions info when team on the first place" in {
      val overcomingRatingTeamName = "test overcoming team"
      val overcomingRatingTeamCity = "test overcoming city"
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1))
        .updated(99, Team(321, "last team", "last city", 100, 100))
        .updated(0, Team(teamId, "team", "city", 49, 1))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(0, Team(teamId, "team", "city", 49, 1))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(0, Team(teamId, "team", "city", 49, 1))
        .updated(1, Team(1, overcomingRatingTeamName, overcomingRatingTeamCity, 47, 2))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Right(TeamPositionsInfo(
        None,
        None,
        TargetTeam(overcomingRatingTeamName, overcomingRatingTeamCity, -2),
        51,
        1,
        49,
        1,
        1
      ))
    }

    "fails when no team in top 500" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1))
        .updated(99, Team(321, "last team", "last city", 100, 100))
        .updated(199, Team(111, "target team", "target city", 51, 199))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(19, Team(teamId, "team", "city", 49, 20))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(29, Team(teamId, "team", "city", 49, 30))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Left("No target team in top 500!")
    }

    "fails when no team in city teams" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1))
        .updated(99, Team(321, "last team", "last city", 100, 100))
        .updated(200, Team(teamId, "team", "city", 49, 200))
        .updated(199, Team(111, "target team", "target city", 51, 199))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(29, Team(teamId, "team", "city", 49, 30))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Left("No target team in city teams!")
    }

    "fails when no team in country teams" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1))
        .updated(99, Team(321, "last team", "last city", 100, 100))
        .updated(200, Team(teamId, "team", "city", 49, 200))
        .updated(199, Team(111, "target team", "target city", 51, 199))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
        .updated(19, Team(teamId, "team", "city", 49, 20))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Left("No target team in country teams!")
    }
  }
}
