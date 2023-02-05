package com.atanana

import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.util
import scala.io.Source
import scala.util.Try

class CommandProvider(socket: ServerSocketChannel) {
  private val buffer = ByteBuffer.allocate(128)

  def getCommand: Try[Option[String]] = {
    Try {
      var command: Option[String] = None
      val socketChannel = socket.accept()
      if socketChannel != null then {
        val read = socketChannel.read(buffer)
        if read > 0 then {
          command = Some(Source.fromBytes(buffer.array()).mkString.trim)
        }
        util.Arrays.fill(buffer.array(), 0.toByte)
        buffer.clear()
        socketChannel.close()
      }
      command
    }
  }
}
