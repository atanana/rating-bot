package com.atanana.processors

import cats.data.EitherT
import com.atanana.checkers.{MainChecker, MainCheckerImpl}
import com.atanana.data.Data
import com.atanana.json.{JsonStore, JsonStoreImpl}
import com.atanana.providers.{PollingDataProvider, PollingDataProviderImpl}
import com.atanana.{CheckResultHandler, CheckResultHandlerImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PollProcessorImpl(
                         pollingDataProvider: PollingDataProvider,
                         store: JsonStore,
                         checker: MainChecker,
                         checkResultHandler: CheckResultHandler
                       ) extends PollProcessor {

  override def process(): EitherT[Future, Throwable, Unit] =
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