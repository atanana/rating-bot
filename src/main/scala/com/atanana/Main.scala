package com.atanana

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel

import com.google.inject.{Guice, Injector}
import com.typesafe.scalalogging.Logger
import net.codingwell.scalaguice.InjectorExtensions._

import scala.io.Source
import scala.util.{Failure, Success}

object Main extends App {
  override def main(args: Array[String]): Unit = {
    val rootInjector = Guice.createInjector(new RatingModule)
    val configurator = rootInjector.instance[Configurator]

    configurator.config match {
      case Success(config) => start(rootInjector, config)
      case Failure(e) => println(e.getMessage)
    }
  }

  private def start(rootInjector: Injector, config: Config) = {
    val logger = Logger("main")
    val injector = rootInjector.createChildInjector(new ConfigModule(config))
    val processor = injector.instance[Processor]
    val serverChannel: ServerSocketChannel = createServerChannel(config)
    val buffer = ByteBuffer.allocate(128)

    while (true) {
      try {
        val socketChannel = serverChannel.accept()
        if (socketChannel != null) {
          val read = socketChannel.read(buffer)
          if (read > 0) {
            val command = Source.fromBytes(buffer.array()).mkString.trim
            processCommand(logger, processor, command)
          }
          buffer.clear()
          socketChannel.close()
        }
      } catch {
        case e: Throwable => logger.debug("Error occurred!", e)
      }
    }
  }

  private def processCommand(logger: Logger, processor: Processor, command: String) = {
    try {
      processor.processCommand(command)
    } catch {
      case e: Throwable => logger.debug("Error occurred!", e)
    }
  }

  private def createServerChannel(config: Config) = {
    val serverChannel = ServerSocketChannel.open()
    serverChannel.socket().bind(new InetSocketAddress(config.port))
    serverChannel.configureBlocking(false)
    serverChannel
  }
}
