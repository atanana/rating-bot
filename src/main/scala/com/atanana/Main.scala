package com.atanana

import java.util.concurrent.TimeUnit

import com.atanana.checkers.MainChecker
import com.atanana.parsers.{CsvParser, RequisitionsParser}
import com.google.inject.Guice
import com.typesafe.scalalogging.Logger
import net.codingwell.scalaguice.InjectorExtensions._

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {
  override def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new RatingModule)
    val configurator = injector.instance[Configurator]

    configurator.config match {
      case Success(config) =>
        val logger = Logger("main")
        val connector = Connector(config)
        val processor = Processor(
          connector,
          injector.instance[CsvParser],
          injector.instance[RequisitionsParser],
          injector.instance[JsonStore],
          injector.instance[MainChecker],
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
}
