package com.atanana.ratingbot

import cats.effect.{ExitCode, IO, IOApp}
import com.atanana.ratingbot.json.Config
import com.typesafe.scalalogging.Logger
import sttp.capabilities.WebSockets
import sttp.client3.SttpBackend
import sttp.client3.httpclient.cats.HttpClientCatsBackend

import java.lang
import java.net.ServerSocket
import scala.util.{Failure, Success}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val isDebug = args.contains("-debug")

    InitModule.jsonConfig.read match {
      case Success(config) =>
        HttpClientCatsBackend.resource[IO]().use { backend =>
          start(config, isDebug, backend)
        }
      case Failure(e) =>
        IO.delay {
          e.printStackTrace()
          ExitCode.Error
        }
    }
  }

  private def start(config: Config, isDebug: Boolean, backend: SttpBackend[IO, WebSockets]): IO[Nothing] = {
    val logger = Logger("main")
    val configModule = new ConfigModule(config, isDebug, backend)
    val processor = configModule.commandProcessor
    val serverSocket = new ServerSocket(config.port)
    val commandProvider = new CommandProvider(serverSocket)

    val task = for
      command <- commandProvider.getCommand
      _ <- processor.processCommand(command)
    yield ()

    task.swap
      .map(logger.debug("Error occurred!", _))
      .value.foreverM
  }
}
