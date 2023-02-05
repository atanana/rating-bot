package com.atanana

import com.atanana.checkers.{MainChecker, MainCheckerImpl, RequisitionsChecker, RequisitionsCheckerImpl, TournamentsChecker, TournamentsCheckerImpl}
import com.atanana.fs.{FsHandler, FsHandlerImpl}
import com.atanana.json.{Config, JsonStore, JsonStoreImpl}
import com.atanana.net.{Connector, ConnectorImpl, NetWrapper, NetWrapperImpl}
import com.atanana.parsers.{CsvParser, ReleasesParser, RequisitionsPageParser, RequisitionsParser, TeamsPageParser, TournamentInfoParser, TournamentPageParser, TournamentPageParserImpl}
import com.atanana.posters.{Poster, RealPoster, TestPoster}
import com.atanana.processors.{CommandProcessor, PollProcessor, PollProcessorImpl, ReminderProcessor, ReminderProcessorImpl, TeamPositionsProcessor, TeamPositionsProcessorImpl}
import com.atanana.providers.{PollingDataProvider, PollingDataProviderImpl, ReleasesProvider, ReleasesProviderImpl, TeamPositionsInfoComposer, TeamPositionsInfoProvider, TeamPositionsInfoProviderImpl, TournamentInfoProvider, TournamentInfoProviderImpl, TournamentPollingFilter, TournamentPollingFilterImpl}

class ConfigModule(config: Config, isDebug: Boolean) {

  import com.softwaremill.macwire._

  lazy val poster: Poster = if (isDebug) wire[TestPoster] else wire[RealPoster]
  lazy val checkResultHandler: CheckResultHandler = wire[CheckResultHandlerImpl]

  lazy val tournamentPollingFilter: TournamentPollingFilter = wire[TournamentPollingFilterImpl]
  lazy val csvParser: CsvParser = wire[CsvParser]
  lazy val tournamentPageParser: TournamentPageParser = wire[TournamentPageParserImpl]
  lazy val requisitionsParser: RequisitionsParser = wire[RequisitionsParser]
  lazy val requisitionsPageParser: RequisitionsPageParser = wire[RequisitionsPageParser]
  lazy val teamsPageParser: TeamsPageParser = wire[TeamsPageParser]
  lazy val tournamentInfoParser: TournamentInfoParser = wire[TournamentInfoParser]
  lazy val releasesParser: ReleasesParser = wire[ReleasesParser]

  lazy val tournamentInfoProvider: TournamentInfoProvider = wire[TournamentInfoProviderImpl]
  lazy val pollingDataProvider: PollingDataProvider = wire[PollingDataProviderImpl]

  lazy val netWrapper: NetWrapper = wire[NetWrapperImpl]
  lazy val connector: Connector = wire[ConnectorImpl]

  lazy val timeProvider: TimeProvider = wire[TimeProvider]
  lazy val releasesProvider: ReleasesProvider = wire[ReleasesProviderImpl]
  lazy val teamPositionsInfoProvider: TeamPositionsInfoProvider = wire[TeamPositionsInfoProviderImpl]
  lazy val teamPositionsInfoComposer = new TeamPositionsInfoComposer(config.team)

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
