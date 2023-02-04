package com.atanana

import com.atanana.checkers.{MainChecker, RequisitionsChecker, RequisitionsCheckerImpl, TournamentsChecker, TournamentsCheckerImpl}
import com.atanana.fs.{FsHandler, FsHandlerImpl}
import com.atanana.json.{Config, JsonStore}
import com.atanana.net.{Connector, ConnectorImpl, NetWrapper, NetWrapperImpl}
import com.atanana.parsers.{CsvParser, ReleasesParser, RequisitionsPageParser, RequisitionsParser, TeamsPageParser, TournamentInfoParser, TournamentPageParser, TournamentPageParserImpl}
import com.atanana.posters.{Poster, RealPoster, TestPoster}
import com.atanana.processors.{CommandProcessor, PollProcessor, ReminderProcessor, TeamPositionsProcessor}
import com.atanana.providers.{PollingDataProvider, ReleasesProvider, TeamPositionsInfoComposer, TeamPositionsInfoProvider, TeamPositionsInfoProviderImpl, TournamentInfoProvider, TournamentInfoProviderImpl, TournamentPollingFilter, TournamentPollingFilterImpl}

class ConfigModule(config: Config, isDebug: Boolean) {

  import com.softwaremill.macwire._

  lazy val poster: Poster = if (isDebug) wire[TestPoster] else wire[RealPoster]
  lazy val checkResultHandler: CheckResultHandler = wire[CheckResultHandler]

  lazy val tournamentPollingFilter: TournamentPollingFilter = wire[TournamentPollingFilterImpl]
  lazy val csvParser: CsvParser = wire[CsvParser]
  lazy val tournamentPageParser: TournamentPageParser = wire[TournamentPageParserImpl]
  lazy val requisitionsParser: RequisitionsParser = wire[RequisitionsParser]
  lazy val requisitionsPageParser: RequisitionsPageParser = wire[RequisitionsPageParser]
  lazy val teamsPageParser: TeamsPageParser = wire[TeamsPageParser]
  lazy val tournamentInfoParser: TournamentInfoParser = wire[TournamentInfoParser]
  lazy val releasesParser: ReleasesParser = wire[ReleasesParser]

  lazy val tournamentInfoProvider: TournamentInfoProvider = wire[TournamentInfoProviderImpl]
  lazy val pollingDataProvider: PollingDataProvider = wire[PollingDataProvider]

  lazy val netWrapper: NetWrapper = wire[NetWrapperImpl]
  lazy val connector: Connector = wire[ConnectorImpl]

  lazy val timeProvider: TimeProvider = wire[TimeProvider]
  lazy val releasesProvider: ReleasesProvider = wire[ReleasesProvider]
  lazy val teamPositionsInfoProvider: TeamPositionsInfoProvider = wire[TeamPositionsInfoProviderImpl]
  lazy val teamPositionsInfoComposer = new TeamPositionsInfoComposer(config.team)

  lazy val fsHandler: FsHandler = wire[FsHandlerImpl]
  lazy val jsonStore: JsonStore = wire[JsonStore]

  lazy val commandProcessor: CommandProcessor = wire[CommandProcessor]
  lazy val pollProcessor: PollProcessor = wire[PollProcessor]
  lazy val reminderProcessor: ReminderProcessor = wire[ReminderProcessor]
  lazy val teamPositionsProcessor: TeamPositionsProcessor = wire[TeamPositionsProcessor]

  lazy val messageComposer: MessageComposer = wire[MessageComposerImpl]

  lazy val mainChecker: MainChecker = wire[MainChecker]
  lazy val tournamentsChecker: TournamentsChecker = wire[TournamentsCheckerImpl]
  lazy val requisitionsChecker: RequisitionsChecker = wire[RequisitionsCheckerImpl]
}
