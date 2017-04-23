package com.atanana.processors

import javax.inject.Inject

import com.atanana.checkers.MainChecker
import com.atanana.providers.PollingDataProvider
import com.atanana.{CheckResultHandler, JsonStore}

class PollProcessor @Inject()(pollingDataProvider: PollingDataProvider, store: JsonStore, checker: MainChecker,
                              checkResultHandler: CheckResultHandler) extends Processor {
  def process(): Unit = {
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

object PollProcessor {
  def apply(pollingDataProvider: PollingDataProvider, jsonStore: JsonStore, mainChecker: MainChecker, checkResultHandler: CheckResultHandler) =
    new PollProcessor(pollingDataProvider, jsonStore, mainChecker, checkResultHandler)
}