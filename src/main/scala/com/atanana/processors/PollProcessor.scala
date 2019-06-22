package com.atanana.processors

import com.atanana.CheckResultHandler
import com.atanana.checkers.MainChecker
import com.atanana.data.{Data, ParsedData}
import com.atanana.json.JsonStore
import com.atanana.providers.PollingDataProvider
import javax.inject.Inject

class PollProcessor @Inject()(pollingDataProvider: PollingDataProvider, store: JsonStore, checker: MainChecker,
                              checkResultHandler: CheckResultHandler) extends Processor {
  override def process(): Unit = {
    val parsedData = pollingDataProvider.data
    val storedData = store.read

    val checkResult = checker.check(storedData, parsedData)
    checkResultHandler.processCheckResult(checkResult)

    if (hasChanges(storedData, parsedData)) {
      store.write(parsedData.toData)
    }
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