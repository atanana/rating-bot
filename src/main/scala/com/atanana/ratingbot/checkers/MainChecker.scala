package com.atanana.ratingbot.checkers

import com.atanana.ratingbot.data.{CheckResult, Data, ParsedData}

trait MainChecker {

  def check(storedData: Data, parsedData: ParsedData): CheckResult
}
