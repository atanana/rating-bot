package com.atanana

import com.atanana.json.{Config, JsonConfig}
import com.atanana.processors.CommandProcessor
import com.google.inject.{Guice, Injector}
import com.typesafe.scalalogging.Logger
import net.codingwell.scalaguice.InjectorExtensions._

import java.net.{ConnectException, InetSocketAddress, SocketTimeoutException}
import java.nio.channels.ServerSocketChannel
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    val rootInjector = Guice.createInjector(new RatingModule)
    val jsonConfig = rootInjector.instance[JsonConfig]
    val isDebug = args.contains("-debug")

    jsonConfig.read match {
      case Success(config) => start(rootInjector, config, isDebug)
      case Failure(e) => println(e.getMessage)
    }
  }

  private def start(rootInjector: Injector, config: Config, isDebug: Boolean): Unit = {
    val logger = Logger("main")
    val injector = rootInjector.createChildInjector(new ConfigModule(config, isDebug))
    val processor = injector.instance[CommandProcessor]
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
        case e: ConnectorException => logger.info(s"Error by url ${e.uri}", e)
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
