package com.atanana

import java.util.concurrent.TimeUnit

import com.google.inject.Guice
import com.typesafe.scalalogging.Logger
import net.codingwell.scalaguice.InjectorExtensions._

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {
  override def main(args: Array[String]): Unit = {
    val rootInjector = Guice.createInjector(new RatingModule)
    val configurator = rootInjector.instance[Configurator]

    configurator.config match {
      case Success(config) =>
        val logger = Logger("main")
        val injector = rootInjector.createChildInjector(new ConfigModule(config))
        val processor = injector.instance[Processor]
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
