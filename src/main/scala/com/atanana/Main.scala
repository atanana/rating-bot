package com.atanana

import java.net.{ConnectException, InetSocketAddress, SocketTimeoutException}
import java.nio.channels.ServerSocketChannel

import com.atanana.json.{Config, JsonConfig}
import com.atanana.processors.CommandProcessor
import com.google.inject.{Guice, Injector}
import com.typesafe.scalalogging.Logger
import net.codingwell.scalaguice.InjectorExtensions._

import scala.util.{Failure, Success}

object Main extends App {
  override def main(args: Array[String]): Unit = {
    val rootInjector = Guice.createInjector(new RatingModule)
    val jsonConfig = rootInjector.instance[JsonConfig]

    jsonConfig.read match {
      case Success(config) => start(rootInjector, config)
      case Failure(e) => println(e.getMessage)
    }
  }

  private def start(rootInjector: Injector, config: Config): Unit = {
    val logger = Logger("main")
    val injector = rootInjector.createChildInjector(new ConfigModule(config))
    val processor = injector.instance[CommandProcessor]
    val serverChannel: ServerSocketChannel = createServerChannel(config)
    val commandProvider = new CommandProvider(serverChannel)

    while (true) {
      try {
        commandProvider.getCommand.get.foreach(processor.processCommand)
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
