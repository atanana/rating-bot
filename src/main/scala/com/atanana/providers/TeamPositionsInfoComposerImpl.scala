package com.atanana.providers

import cats.implicits.toFoldableOps
import com.atanana.data.{TargetTeam, Team, TeamPositionsInfo}
import com.atanana.json.Config

class TeamPositionsInfoComposerImpl(config: Config) extends TeamPositionsInfoComposer {
  override def positionsInfo(teams: List[Team], cityTeams: List[Team], countryTeams: List[Team]): Either[String, TeamPositionsInfo] = {
    for
      lastTeam <- getLastTop100Team(teams)
      team <- findTeam(teams)
      targetTeam <- findTargetTeam(teams, "all")
      targetCountryTeam <- findTargetTeam(countryTeams, "country")
      overcomingCountryTeam <- findOvercomingTeam(countryTeams)
      cityPosition <- getPosition(cityTeams, "city")
      countryPosition <- getPosition(countryTeams, "country")
    yield TeamPositionsInfo(
      targetTeam.map(TargetTeam(_, team)),
      targetCountryTeam.map(TargetTeam(_, team)),
      TargetTeam(overcomingCountryTeam, team),
      lastTeam.rating - team.rating,
      team.position,
      team.rating,
      cityPosition,
      countryPosition
    )
  }

  private def getLastTop100Team(teams: List[Team]) =
    teams.find(_.position > 99.9).toRight("No last team!")

  private def findTargetTeam(teams: List[Team], listName: String): Either[String, Option[Team]] =
    findTeamIndex(teams, listName).map(index => teams.get(index - 1))

  private def findTeamIndex(teams: List[Team], listName: String) =
    teams.zipWithIndex
      .find({ case (team, _) => team.id == config.team })
      .map({ case (_, i) => i })
      .toRight(s"No target team in $listName teams!")

  private def findOvercomingTeam(teams: List[Team]): Either[String, Team] =
    findTeamIndex(teams, "country").map(_ + 1).map(teams(_))

  private def findTeam(teams: List[Team]) =
    teams.find(_.id == config.team).toRight("No target team in top 500!")

  private def getPosition(teams: List[Team], listName: String) =
    findTeamIndex(teams, listName).map(_ + 1)
}
