package com.atanana.processors

import cats.data.EitherT
import com.atanana.CheckResultHandler
import com.atanana.checkers.MainChecker
import com.atanana.data.Data
import com.atanana.json.JsonStore
import com.atanana.providers.PollingDataProvider

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PollProcessor @Inject()(
                               pollingDataProvider: PollingDataProvider,
                               store: JsonStore,
                               checker: MainChecker,
                               checkResultHandler: CheckResultHandler
                             ) extends Processor {

  override def process(): EitherT[Future, Throwable, Unit] =
    for {
      parsedData <- pollingDataProvider.data
      storedData = store.read
      checkResult = checker.check(storedData, parsedData)
      _ <- checkResultHandler.processCheckResult(checkResult)
    } yield {
      if (hasChanges(storedData, parsedData)) {
        store.write(parsedData)
      }
    }

  private def hasChanges(storedData: Data, parsedData: Data) =
    storedData.tournaments != parsedData.tournaments || storedData.requisitions != parsedData.requisitions
}