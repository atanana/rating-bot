package com.atanana

import java.util.concurrent.TimeUnit

import com.atanana.checkers.{MainChecker, RequisitionsChecker, TournamentsChecker}
import com.atanana.parsers.{CsvParser, RequisitionsParser}
import com.google.inject.Guice
import com.typesafe.scalalogging.Logger
import net.codingwell.scalaguice.InjectorExtensions._

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {
  override def main(args: Array[String]): Unit = Configurator(new SystemWrapper).config match {
    case Success(config) =>
      val logger = Logger("main")
      val injector = Guice.createInjector(new RatingModule)
      val connector = Connector(config)
      val processor = Processor(
        connector,
        CsvParser(),
        RequisitionsParser(),
        injector.instance[JsonStore],
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
