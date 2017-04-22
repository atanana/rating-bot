package com.atanana

import javax.inject.Inject

import com.atanana.checkers.MainChecker
import com.atanana.providers.PollingDataProvider

class Processor @Inject()(pollingDataProvider: PollingDataProvider, store: JsonStore, checker: MainChecker,
                          checkResultHandler: CheckResultHandler) {
  def processCommand(command: String): Unit = command match {
    case "poll" => process()
    case _ => throw new RuntimeException(s"Unknown command $command!")
  }

  private def process(): Unit = {
    val parsedData = pollingDataProvider.data
    val storedData = store.read

    val checkResult = checker.check(storedData, parsedData)
    checkResultHandler.processCheckResult(checkResult)

    val newData = parsedData.toData
    if (storedData != newData) {
      store.write(newData)
    }
  }
}

object Processor {
  def apply(pollingDataProvider: PollingDataProvider, jsonStore: JsonStore, mainChecker: MainChecker, checkResultHandler: CheckResultHandler) =
    new Processor(pollingDataProvider, jsonStore, mainChecker, checkResultHandler)
}