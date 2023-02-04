package com.atanana

import com.atanana.json.Config
import com.typesafe.scalalogging.Logger

import java.net.{ConnectException, InetSocketAddress, SocketTimeoutException}
import java.nio.channels.ServerSocketChannel
import scala.concurrent.Await
import scala.concurrent.duration._
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
    val serverChannel: ServerSocketChannel = createServerChannel(config)
    val commandProvider = new CommandProvider(serverChannel)

    while (true) {
      try {
        for {
          command <- commandProvider.getCommand.get
          future = processor.processCommand(command).value
          result = Await.result(future, 10.minutes)
        } yield result.left.map(logger.error("Error!", _))
      } catch {
        case e: SocketTimeoutException => logger.info("Timeout!", e)
        case e: ConnectException => logger.info("Connect error!", e)
        case e: Throwable => logger.debug("Error occurred!", e)
      }
    }
  }

  private def createServerChannel(config: Config) = {
    val serverChannel = ServerSocketChannel.open()
    serverChannel.socket().bind(new InetSocketAddress(config.port))
    serverChannel.configureBlocking(false)
    serverChannel
  }
}
