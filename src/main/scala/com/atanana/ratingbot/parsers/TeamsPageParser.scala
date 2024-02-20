package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.Team

trait TeamsPageParser {

  def getTeams(teamsContent: String): List[Team]
}
