package com.atanana

import com.atanana.data.Editor
import com.atanana.parsers.TournamentPageParser
import com.atanana.providers.TournamentInfoProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

class TournamentInfoProviderTest extends WordSpecLike with MockFactory with Matchers with BeforeAndAfter {
  var connector: Connector = _
  var parser: TournamentPageParser = _

  var provider: TournamentInfoProvider = _

  before {
    connector = stub[Connector]
    parser = stub[TournamentPageParser]
    provider = new TournamentInfoProvider(connector, parser)
  }

  "TournamentInfoProvider" should {
    "provide correct info" in {
      val tournamentId = 123
      val page = "tournament page"
      val editor = mock[Editor]
      (connector.getTournamentPage _).when(tournamentId).returns(Right(page))
      (parser.getEditors _).when(page).returns(List(editor))

      provider.getEditors(tournamentId) shouldEqual List(editor)
    }
  }
}
