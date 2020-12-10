package com.atanana.processors

import cats.data.EitherT
import com.atanana.CheckResultHandler
import com.atanana.checkers.MainChecker
import com.atanana.data.{Data, ParsedData}
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

  override def process(): EitherT[Future, Throwable, Unit] = {
    val result = for {
      parsedDataEither <- pollingDataProvider.data
    } yield for {
      parsedData <- parsedDataEither
      storedData = store.read
      checkResult = checker.check(storedData, parsedData)
      _ <- checkResultHandler.processCheckResult(checkResult)
    } yield {
      if (hasChanges(storedData, parsedData)) {
        store.write(parsedData.toData)
      }
    }
    EitherT(result).leftMap(new RuntimeException(_))
  }

  private def hasChanges(storedData: Data, parsedData: ParsedData) =
    tournamentsChanged(storedData, parsedData) || requisitionsChanged(storedData, parsedData)

  private def requisitionsChanged(storedData: Data, parsedData: ParsedData) =
    parsedData.requisitions.fold(
      _ => false,
      requisitions => storedData.requisitions != requisitions.map(_.toRequisition)
    )

  private def tournamentsChanged(storedData: Data, parsedData: ParsedData) =
    storedData.tournaments != parsedData.tournaments.map(_.toTournament)
}