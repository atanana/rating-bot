package com.atanana.processors

import javax.inject.Inject

import com.atanana.CheckResultHandler
import com.atanana.checkers.MainChecker
import com.atanana.json.JsonStore
import com.atanana.providers.PollingDataProvider

class PollProcessor @Inject()(pollingDataProvider: PollingDataProvider, store: JsonStore, checker: MainChecker,
                              checkResultHandler: CheckResultHandler) extends Processor {
  override def process(): Unit = {
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