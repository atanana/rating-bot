package com.atanana

import java.nio.ByteBuffer
import java.nio.channels.spi.SelectorProvider
import java.nio.channels.{ServerSocketChannel, SocketChannel}

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpecLike}

import scala.util.Success

class CommandProviderTest extends WordSpecLike with MockFactory with BeforeAndAfter with Matchers {
  var socket: ServerSocketChannel = _
  var socketChannel: SocketChannel = _
  var provider: CommandProvider = _

  before {
    socket = stub[TestSocket]
    socketChannel = stub[TestSocketChannel]
    provider = new CommandProvider(socket)
  }

  "CommandProvider" should {
    "provide no command when socket is null" in {
      (socket.accept _).when().returns(null)
      provider.getCommand shouldEqual Success(None)
    }

    "provide no command when socket is empty" in {
      (socket.accept _).when().returns(socketChannel)
      (socketChannel.read(_: ByteBuffer)).when(*).returns(0)
      provider.getCommand shouldEqual Success(None)
    }

    "provide valid command" in {
      (socket.accept _).when().returns(socketChannel)
      (socketChannel.read(_: ByteBuffer)).when(*).onCall((buffer: ByteBuffer) => {
        buffer.put("test command\n".getBytes)
        123
      })
      provider.getCommand shouldEqual Success(Some("test command"))
    }
  }
}

abstract private class TestSocket(provider: SelectorProvider) extends ServerSocketChannel(provider) {
  protected def implConfigureBlocking(block: Boolean): Unit

  protected def implCloseSelectableChannel(): Unit
}

abstract private class TestSocketChannel(provider: SelectorProvider) extends SocketChannel(provider) {
  protected def implConfigureBlocking(block: Boolean): Unit

  protected def implCloseSelectableChannel(): Unit
}