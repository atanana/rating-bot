package com.atanana.parsers

import com.atanana.types.Pages.TeamTournamentsPage
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.util.Success

class TeamTournamentsParserImplTest extends AnyWordSpecLike with Matchers {

  val parser = new TeamTournamentsParserImpl()

  "TeamTournamentsParser" should {

    "parse team tournaments" in {
      val json =
        """[
          |  {
          |    "idteam": 51739,
          |    "idtournament": 3325
          |  },
          |  {
          |    "idteam": 51739,
          |    "idtournament": 3325
          |  },
          |  {
          |    "idteam": 51739,
          |    "idtournament": 3326
          |  },
          |  {
          |    "idteam": 51739,
          |    "idtournament": 3327
          |  }
          |]""".stripMargin
      parser.getTournamentIds(TeamTournamentsPage(json)) shouldEqual Success(Set(3325, 3326, 3327))
    }
  }
}
