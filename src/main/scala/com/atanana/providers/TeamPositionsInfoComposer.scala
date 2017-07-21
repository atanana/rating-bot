package com.atanana.providers

import com.atanana.data.{TargetTeam, Team, TeamPositionsInfo}

import scala.util.Try

class TeamPositionsInfoComposer(teamId: Int) {
  def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Try[TeamPositionsInfo] = {
    Try {
      val lastTeam = teams(99)
      val team = findTeam(teams)
      TeamPositionsInfo(
        TargetTeam(findTargetTeam(teams), team),
        TargetTeam(findTargetTeam(countryTeams), team),
        TargetTeam(findOvercomingTeam(countryTeams, team), team),
        lastTeam.rating - team.rating,
        team.position,
        team.rating,
        getPosition(cityTeams, "city"),
        getPosition(countryTeams, "country")
      )
    }
  }

  private def findTargetTeam(teams: List[Team]) = {
    teams(teams.indexWhere(_.id == teamId) - 1)
  }

  private def findOvercomingTeam(teams: List[Team], team: Team) = {
    teams(teams.indexWhere(_.id == teamId) + 1)
  }

  private def findTeam(teams: List[Team]) = {
    teams.find(_.id == teamId)
      .getOrElse(throw new RuntimeException("No target team in top 500!"))
  }

  private def getPosition(teams: List[Team], listName: String) = {
    val result = teams.indexWhere(_.id == teamId)
    if (result == -1) {
      throw new RuntimeException(s"No target team in $listName teams!")
    } else {
      result + 1
    }
  }
}
