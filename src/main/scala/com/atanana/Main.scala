package com.atanana

import java.util.concurrent.TimeUnit

import com.atanana.checkers.{MainChecker, RequisitionsChecker, TournamentsChecker}
import com.atanana.parsers.{CsvParser, RequisitionsParser}
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {
  override def main(args: Array[String]): Unit = Configurator(new SystemWrapper).config match {
    case Success(config) =>
      val logger = Logger("main")
      val connector = Connector(config)
      val processor = Processor(
        connector,
        CsvParser(),
        RequisitionsParser(),
        JsonStore(new FsHandler),
        MainChecker(TournamentsChecker(), RequisitionsChecker()),
        CheckResultHandler(Poster(connector, config), MessageComposer())
      )

      while (true) {
        try {
          processor.process()
        } catch {
          case e: Throwable => logger.debug("Error occurred!", e)
        }

        Thread.sleep(Duration(30, TimeUnit.SECONDS).toMillis)
      }
    case Failure(e) => println(e.getMessage)
  }
}
