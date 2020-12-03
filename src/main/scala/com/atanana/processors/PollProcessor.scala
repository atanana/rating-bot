package com.atanana.processors

import com.atanana.CheckResultHandler
import com.atanana.checkers.MainChecker
import com.atanana.data.{Data, ParsedData}
import com.atanana.json.JsonStore
import com.atanana.processors.PollProcessor.logger
import com.atanana.providers.PollingDataProvider
import com.typesafe.scalalogging.Logger

import javax.inject.Inject

class PollProcessor @Inject()(
                               pollingDataProvider: PollingDataProvider,
                               store: JsonStore,
                               checker: MainChecker,
                               checkResultHandler: CheckResultHandler
                             ) extends Processor {

  override def process(): Either[String, Unit] = {
    val parsedData = pollingDataProvider.data.fold(
      error => {
        logger.error(error)
        return Right()
      },
      identity
    )
    val storedData = store.read

    val checkResult = checker.check(storedData, parsedData)
    val isPostSuccessful = checkResultHandler.processCheckResult(checkResult).isRight

    if (isPostSuccessful && hasChanges(storedData, parsedData)) {
      store.write(parsedData.toData)
    }
    Right()
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

object PollProcessor {
  private val logger = Logger(classOf[PollProcessor])
}