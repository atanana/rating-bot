package com.atanana.ratingbot.processors

import cats.data.EitherT
import cats.effect.IO
import com.atanana.ratingbot.checkers.{MainChecker, MainCheckerImpl}
import com.atanana.ratingbot.data.Data
import com.atanana.ratingbot.json.{JsonStore, JsonStoreImpl}
import com.atanana.ratingbot.providers.{PollingDataProvider, PollingDataProviderImpl}
import com.atanana.ratingbot.{CheckResultHandler, CheckResultHandlerImpl}

class PollProcessorImpl(
                         pollingDataProvider: PollingDataProvider,
                         store: JsonStore,
                         checker: MainChecker,
                         checkResultHandler: CheckResultHandler
                       ) extends PollProcessor {

  override def process(): EitherT[IO, Throwable, Unit] =
    for
      parsedData <- pollingDataProvider.data
      storedData = store.read
      checkResult = checker.check(storedData, parsedData)
      _ <- checkResultHandler.processCheckResult(checkResult)
    yield {
      val finalData = addMissingTournaments(storedData, parsedData)
      if hasChanges(storedData, finalData) then {
        store.write(finalData)
      }
    }

  private def addMissingTournaments(storedData: Data, parsedData: Data): Data = {
    val parsedIds = parsedData.tournaments.map(_.id)
    var newTournaments = parsedData.tournaments
    for tournament <- storedData.tournaments do {
      if !parsedIds.contains(tournament.id) then {
        newTournaments += tournament
      }
    }

    parsedData.copy(tournaments = newTournaments)
  }

  private def hasChanges(storedData: Data, parsedData: Data) =
    storedData.tournaments != parsedData.tournaments || storedData.requisitions != parsedData.requisitions
}