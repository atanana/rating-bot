package com.atanana.ratingbot

import com.typesafe.scalalogging.Logger

import java.io.{BufferedReader, InputStreamReader}
import java.net.ServerSocket
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

class CommandProvider(serverSocket: ServerSocket) {

  private val logger = Logger("CommandProvider")

  def getCommand: Try[String] =
    try {
      val socket = serverSocket.accept()
      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream))
      Success(reader.readLine())
    }
    catch {
      case NonFatal(e) =>
        logger.error("Error in socket!", e)
        Failure(e)
    }
}
