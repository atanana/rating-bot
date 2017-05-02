package com.atanana.providers

import com.atanana.data.{Team, TeamPositionInfo}

import scala.util.Try

class TeamPositionsProvider(teamId: Int) {
  def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Try[TeamPositionInfo] = {
    Try {
      val lastTeam = teams(99)
      val team = teams.find(_.id == teamId)
        .getOrElse(throw new RuntimeException("No target team in top 500!"))
      val targetTeam = teams(teams.indexOf(team) - 1)
      TeamPositionInfo(
        targetTeam.name,
        targetTeam.city,
        lastTeam.rating - team.rating,
        team.position,
        team.rating,
        getPosition(cityTeams, "city"),
        getPosition(countryTeams, "country")
      )
    }
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
