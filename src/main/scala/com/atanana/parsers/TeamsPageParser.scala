package com.atanana.parsers

import com.atanana.data.Team

trait TeamsPageParser {

  def getTeams(teamsContent: String): List[Team]
}
