package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.data.Team
import com.atanana.ratingbot.parsers.TeamsPageParser

import scala.collection.mutable

class MockTeamsPageParser extends TeamsPageParser {

  val teams: mutable.Map[String, List[Team]] = mutable.Map()

  override def getTeams(teamsContent: String): List[Team] = teams(teamsContent)
}
