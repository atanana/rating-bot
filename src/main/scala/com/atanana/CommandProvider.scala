package com.atanana

import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.util
import scala.io.Source
import scala.util.Try
import cats.implicits.*

import java.io.{BufferedInputStream, BufferedReader, DataInputStream, InputStreamReader}
import java.net.ServerSocket

class CommandProvider(serverSocket: ServerSocket) {

  def getCommand: Try[String] = {
    Try {
      val socket = serverSocket.accept()
      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream))
      reader.readLine()
    }
  }
}
