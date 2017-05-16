package com.atanana.providers

import com.atanana.data.{Team, TeamPositionsInfo}
import org.scalatest.{Matchers, WordSpecLike}

import scala.util.{Failure, Success}

class TeamPositionsInfoComposerTest extends WordSpecLike with Matchers {
  val teamId = 123
  val provider = new TeamPositionsInfoComposer(teamId)

  "TeamPositionsInfoComposer" should {
    "provide correct positions info" in {
      val teams = List.fill(500)(Team(1, "test team", "test city", 1, 1f))
        .updated(99, Team(321, "last team", "last city", 100, 100f))
        .updated(200, Team(teamId, "team", "city", 49, 200f))
        .updated(199, Team(111, "target team", "target city", 51, 199f))
      val cityTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(19, Team(teamId, "team", "city", 49, 20f))
      val countryTeams = List.fill(100)(Team(1, "test team", "test city", 1, 1f))
        .updated(29, Team(teamId, "team", "city", 49, 30f))
      provider.positionsInfo(teams, cityTeams, countryTeams) shouldEqual Success(TeamPositionsInfo(
        "target team",
        "target city",
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
