package com.atanana

import com.atanana.checkers.{MainChecker, RequisitionsChecker, TournamentsChecker}
import com.atanana.json.{JsonConfig, JsonStore}
import com.atanana.parsers.{CsvParser, RequisitionsParser}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class RatingModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[FsHandler]
    bind[JsonStore]
    bind[JsonConfig]
    bind[MessageComposer]

    bind[CsvParser]
    bind[RequisitionsParser]

    bind[MainChecker]
    bind[TournamentsChecker]
    bind[RequisitionsChecker]
  }
}
