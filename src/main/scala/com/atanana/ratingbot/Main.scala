package com.atanana.ratingbot

import com.atanana.ratingbot.json.Config
import com.atanana.ratingbot.{CommandProvider, ConfigModule, InitModule}
import com.typesafe.scalalogging.Logger

import java.net.{ConnectException, InetSocketAddress, ServerSocket, SocketTimeoutException}
import java.nio.channels.ServerSocketChannel
import scala.concurrent.Await
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    val isDebug = args.contains("-debug")

    InitModule.jsonConfig.read match {
      case Success(config) => start(config, isDebug)
      case Failure(e) => println(e.getMessage)
    }
  }

  private def start(config: Config, isDebug: Boolean): Unit = {
    val logger = Logger("main")
    val configModule = new ConfigModule(config, isDebug)
    val processor = configModule.commandProcessor
    val serverSocket = new ServerSocket(config.port)
    val commandProvider = new CommandProvider(serverSocket)

    while true do {
      try {
        for
          command <- commandProvider.getCommand
          future = processor.processCommand(command).value
          result = Await.result(future, 10.minutes)
        yield result.left.map(logger.error("Error!", _))
      } catch {
        case e: SocketTimeoutException => logger.info("Timeout!", e)
        case e: ConnectException => logger.info("Connect error!", e)
        case e: Throwable => logger.debug("Error occurred!", e)
      }
    }
  }
}
