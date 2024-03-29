package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.checkers.MainChecker
import com.atanana.ratingbot.data.{CheckResult, Data, ParsedData}

import scala.collection.mutable

class MockMainChecker extends MainChecker {

  var results: mutable.Map[(Data, ParsedData), CheckResult] = mutable.Map()

  override def check(storedData: Data, parsedData: ParsedData): CheckResult = results((storedData, parsedData))
}
