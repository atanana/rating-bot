package com.atanana

import com.atanana.checkers.{MainChecker, MainCheckerImpl, RequisitionsChecker, RequisitionsCheckerImpl, TournamentsChecker, TournamentsCheckerImpl}
import com.atanana.fs.{FsHandler, FsHandlerImpl}
import com.atanana.json.{Config, JsonStore, JsonStoreImpl}
import com.atanana.net.{Connector, ConnectorImpl, NetWrapper, NetWrapperImpl}
import com.atanana.parsers.{CsvParser, CsvParserImpl, ReleasesParser, ReleasesParserImpl, RequisitionsPageParser, RequisitionsParser, TeamsPageParser, TeamsPageParserImpl, TournamentInfoParser, TournamentPageParser, TournamentPageParserImpl}
import com.atanana.posters.{Poster, RealPoster, TestPoster}
import com.atanana.processors.{CommandProcessor, PollProcessor, PollProcessorImpl, ReminderProcessor, ReminderProcessorImpl, TeamPositionsProcessor, TeamPositionsProcessorImpl}
import com.atanana.providers.{PollingDataProvider, PollingDataProviderImpl, ReleasesProvider, ReleasesProviderImpl, TeamPositionsInfoComposer, TeamPositionsInfoComposerImpl, TeamPositionsInfoProvider, TeamPositionsInfoProviderImpl, TournamentInfoProvider, TournamentInfoProviderImpl, TournamentPollingFilter, TournamentPollingFilterImpl}

class ConfigModule(config: Config, isDebug: Boolean) {

  import com.softwaremill.macwire._

  lazy val poster: Poster = if (isDebug) wire[TestPoster] else wire[RealPoster]
  lazy val checkResultHandler: CheckResultHandler = wire[CheckResultHandlerImpl]

  lazy val tournamentPollingFilter: TournamentPollingFilter = wire[TournamentPollingFilterImpl]
  lazy val csvParser: CsvParser = wire[CsvParserImpl]
  lazy val tournamentPageParser: TournamentPageParser = wire[TournamentPageParserImpl]
  lazy val requisitionsParser: RequisitionsParser = wire[RequisitionsParser]
  lazy val requisitionsPageParser: RequisitionsPageParser = wire[RequisitionsPageParser]
  lazy val teamsPageParser: TeamsPageParser = wire[TeamsPageParserImpl]
  lazy val tournamentInfoParser: TournamentInfoParser = wire[TournamentInfoParser]
  lazy val releasesParser: ReleasesParser = wire[ReleasesParserImpl]

  lazy val tournamentInfoProvider: TournamentInfoProvider = wire[TournamentInfoProviderImpl]
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
