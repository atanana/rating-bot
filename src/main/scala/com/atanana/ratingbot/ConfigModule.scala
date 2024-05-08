package com.atanana.ratingbot

import cats.effect.IO
import com.atanana.ratingbot.*
import com.atanana.ratingbot.checkers.*
import com.atanana.ratingbot.fs.{FsHandler, FsHandlerImpl}
import com.atanana.ratingbot.json.{Config, JsonStore, JsonStoreImpl}
import com.atanana.ratingbot.net.{Connector, ConnectorImpl, NetWrapper, NetWrapperImpl}
import com.atanana.ratingbot.parsers.*
import com.atanana.ratingbot.posters.{Poster, RealPoster, TestPoster}
import com.atanana.ratingbot.processors.*
import com.atanana.ratingbot.providers.*
import sttp.capabilities.WebSockets
import sttp.client3.SttpBackend

class ConfigModule(config: Config, isDebug: Boolean, backend: SttpBackend[IO, WebSockets]) {

  import com.softwaremill.macwire.*

  lazy val poster: Poster = if isDebug then wire[TestPoster] else wire[RealPoster]
  lazy val checkResultHandler: CheckResultHandler = wire[CheckResultHandlerImpl]

  lazy val tournamentPageParser: TournamentPageParser = wire[TournamentPageParserImpl]
  lazy val requisitionsParser: RequisitionsParser = wire[RequisitionsParserImpl]
  lazy val requisitionsPageParser: RequisitionsPageParser = wire[RequisitionsPageParserImpl]
  lazy val teamsPageParser: TeamsPageParser = wire[TeamsPageParserImpl]
  lazy val tournamentInfoParser: TournamentInfoParser = wire[TournamentInfoParserImpl]
  lazy val releasesParser: ReleasesParser = wire[ReleasesParserImpl]
  lazy val teamTournamentsParser: TeamTournamentsParser = wire[TeamTournamentsParserImpl]
  lazy val tournamentResultsParser: TournamentResultsParser = wire[TournamentResultsParserImpl]

  lazy val tournamentInfoProvider: TournamentInfoProvider = wire[TournamentInfoProviderImpl]
  lazy val lastTeamResultsProvider: LastTeamResultsProvider = wire[LastTeamResultsProviderImpl]
  lazy val pollingDataProvider: PollingDataProvider = wire[PollingDataProviderImpl]

  lazy val netWrapper: NetWrapper = wire[NetWrapperImpl]
  lazy val connector: Connector = wire[ConnectorImpl]

  lazy val timeProvider: TimeProvider = wire[TimeProviderImpl]
  lazy val releasesProvider: ReleasesProvider = wire[ReleasesProviderImpl]
  lazy val teamPositionsInfoComposer: TeamPositionsInfoComposer = wire[TeamPositionsInfoComposerImpl]
  lazy val teamPositionsInfoProvider: TeamPositionsInfoProvider = wire[TeamPositionsInfoProviderImpl]

  lazy val fsHandler: FsHandler = wire[FsHandlerImpl]
  lazy val jsonStore: JsonStore = wire[JsonStoreImpl]

  lazy val commandProcessor: CommandProcessor = wire[CommandProcessor]
  lazy val pollProcessor: PollProcessor = wire[PollProcessorImpl]
  lazy val reminderProcessor: ReminderProcessor = wire[ReminderProcessorImpl]
  lazy val teamPositionsProcessor: TeamPositionsProcessor = wire[TeamPositionsProcessorImpl]

  lazy val messageComposer: MessageComposer = wire[MessageComposerImpl]

  lazy val mainChecker: MainChecker = wire[MainCheckerImpl]
  lazy val tournamentsChecker: TournamentsChecker = wire[TournamentsCheckerImpl]
  lazy val requisitionsChecker: RequisitionsChecker = wire[RequisitionsCheckerImpl]
}
