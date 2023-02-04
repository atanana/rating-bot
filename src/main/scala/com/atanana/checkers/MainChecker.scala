package com.atanana.checkers

import com.atanana.data.{CheckResult, Data, ParsedData}

trait MainChecker {

  def check(storedData: Data, parsedData: ParsedData): CheckResult
}
