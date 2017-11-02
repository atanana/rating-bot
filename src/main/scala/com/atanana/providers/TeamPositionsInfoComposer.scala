package com.atanana.providers

import com.atanana.data.{Team, TeamPositionsInfo}

import scala.util.Try

class TeamPositionsInfoComposer(teamId: Int) {
  def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Try[TeamPositionsInfo] = {
    Try {
      val lastTeam = getLastTop100Team(teams)
      val team = teams.find(_.id == teamId)
        .getOrElse(throw new RuntimeException("No target team in top 500!"))
      val targetTeam = teams(teams.indexOf(team) - 1)
      TeamPositionsInfo(
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

  private def getLastTop100Team(teams: List[Team]) = {
    teams.find(_.position > 99.9).get
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
