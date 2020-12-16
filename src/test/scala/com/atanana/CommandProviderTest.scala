package com.atanana

import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.nio.ByteBuffer
import java.nio.channels.spi.SelectorProvider
import java.nio.channels.{ServerSocketChannel, SocketChannel}
import scala.util.Success

class CommandProviderTest extends AnyWordSpecLike with MockFactory with Matchers {
  private val socket = stub[TestSocket]
  private val socketChannel = stub[TestSocketChannel]
  private val provider = new CommandProvider(socket)

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
      }).noMoreThanOnce()
      provider.getCommand shouldEqual Success(Some("test command"))

      //check that buffer was cleared
      (socketChannel.read(_: ByteBuffer)).when(*).onCall((buffer: ByteBuffer) => {
        buffer.put("tttt\n".getBytes)
        123
      }).noMoreThanOnce()
      provider.getCommand shouldEqual Success(Some("tttt"))
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