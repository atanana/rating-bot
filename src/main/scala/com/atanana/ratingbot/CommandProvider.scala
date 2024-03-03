package com.atanana.ratingbot

import java.io.{BufferedReader, InputStreamReader}
import java.net.ServerSocket
import scala.util.Try

class CommandProvider(serverSocket: ServerSocket) {

  def getCommand: Try[String] = {
    Try {
      val socket = serverSocket.accept()
      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream))
      reader.readLine()
    }
  }
}
