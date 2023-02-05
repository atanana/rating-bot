package com.atanana.mocks

import com.atanana.data.Team
import com.atanana.parsers.TeamsPageParser

import scala.collection.mutable

class MockTeamsPageParser extends TeamsPageParser {

  val teams: mutable.Map[String, List[Team]] = mutable.Map()

  override def getTeams(teamsContent: String): List[Team] = teams(teamsContent)
}
