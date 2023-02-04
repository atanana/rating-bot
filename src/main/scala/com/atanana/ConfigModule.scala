package com.atanana

import com.atanana.checkers.{MainChecker, RequisitionsChecker, TournamentsChecker}
import com.atanana.json.{Config, JsonStore}
import com.atanana.parsers.{CsvParser, ReleasesParser, RequisitionsPageParser, RequisitionsParser, TeamsPageParser, TournamentInfoParser, TournamentPageParser}
import com.atanana.posters.{Poster, RealPoster, TestPoster}
import com.atanana.processors.{CommandProcessor, PollProcessor, ReminderProcessor, TeamPositionsProcessor}
import com.atanana.providers.{PollingDataProvider, ReleasesProvider, TeamPositionsInfoComposer, TeamPositionsInfoProvider, TournamentInfoProvider, TournamentPollingFilter}

class ConfigModule(config: Config, isDebug: Boolean) {

  import com.softwaremill.macwire._

  lazy val poster: Poster = if (isDebug) wire[TestPoster] else wire[RealPoster]
  lazy val checkResultHandler: CheckResultHandler = wire[CheckResultHandler]

  lazy val tournamentPollingFilter: TournamentPollingFilter = wire[TournamentPollingFilter]
  lazy val csvParser: CsvParser = wire[CsvParser]
  lazy val tournamentPageParser: TournamentPageParser = wire[TournamentPageParser]
  lazy val requisitionsParser: RequisitionsParser = wire[RequisitionsParser]
  lazy val requisitionsPageParser: RequisitionsPageParser = wire[RequisitionsPageParser]
  lazy val teamsPageParser: TeamsPageParser = wire[TeamsPageParser]
  lazy val tournamentInfoParser: TournamentInfoParser = wire[TournamentInfoParser]
  lazy val releasesParser: ReleasesParser = wire[ReleasesParser]

  lazy val tournamentInfoProvider: TournamentInfoProvider = wire[TournamentInfoProvider]
  lazy val pollingDataProvider: PollingDataProvider = wire[PollingDataProvider]

  lazy val netWrapper: NetWrapper = wire[NetWrapper]
  lazy val connector: Connector = wire[Connector]

  lazy val timeProvider: TimeProvider = wire[TimeProvider]
  lazy val releasesProvider: ReleasesProvider = wire[ReleasesProvider]
  lazy val teamPositionsInfoProvider: TeamPositionsInfoProvider = wire[TeamPositionsInfoProvider]
  lazy val teamPositionsInfoComposer = new TeamPositionsInfoComposer(config.team)

  lazy val fsHandler: FsHandler = wire[FsHandler]
  lazy val jsonStore: JsonStore = wire[JsonStore]

  lazy val commandProcessor: CommandProcessor = wire[CommandProcessor]
  lazy val pollProcessor: PollProcessor = wire[PollProcessor]
  lazy val reminderProcessor: ReminderProcessor = wire[ReminderProcessor]
  lazy val teamPositionsProcessor: TeamPositionsProcessor = wire[TeamPositionsProcessor]

  lazy val messageComposer: MessageComposer = wire[MessageComposer]

  lazy val mainChecker: MainChecker = wire[MainChecker]
  lazy val tournamentsChecker: TournamentsChecker = wire[TournamentsChecker]
  lazy val requisitionsChecker: RequisitionsChecker = wire[RequisitionsChecker]
}
