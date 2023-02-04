package com.atanana

import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.net.{ServerSocket, Socket, SocketAddress, SocketOption}
import java.nio.ByteBuffer
import java.nio.channels.spi.SelectorProvider
import java.nio.channels.{ServerSocketChannel, SocketChannel}
import java.util
import scala.util.Success

class CommandProviderTest extends AnyWordSpecLike with Matchers with BeforeAndAfter {
  private val socket = new TestSocket(null)
  private val socketChannel = new TestSocketChannel(null)
  private val provider = new CommandProvider(socket)

  after {
    socketChannel.reset()
  }

  "CommandProvider" should {
    "provide no command when socket is null" in {
      socket.socketChannel = null
      provider.getCommand shouldEqual Success(None)
    }

    "provide no command when socket is empty" in {
      socket.socketChannel = socketChannel
      socketChannel.bytes = Array.emptyByteArray
      socketChannel.readResult = 0
      provider.getCommand shouldEqual Success(None)
    }

    "provide valid command" in {
      socket.socketChannel = socketChannel

      socketChannel.bytes = "test command\n".getBytes
      socketChannel.readResult = 123

      provider.getCommand shouldEqual Success(Some("test command"))
      socketChannel.readCount shouldEqual 1

      //check that buffer was cleared
      socketChannel.bytes = "tttt\n".getBytes
      socketChannel.readResult = 123

      provider.getCommand shouldEqual Success(Some("tttt"))
      socketChannel.readCount shouldEqual 2
    }
  }
}

class TestSocket(provider: SelectorProvider) extends ServerSocketChannel(provider) {

  var socketChannel: SocketChannel = _

  override def accept(): SocketChannel = socketChannel

  override def bind(local: SocketAddress, backlog: Int): ServerSocketChannel = ???

  override def setOption[T](name: SocketOption[T], value: T): ServerSocketChannel = ???

  override def socket(): ServerSocket = ???

  override def getLocalAddress: SocketAddress = ???

  override def getOption[T](name: SocketOption[T]): T = ???

  override def supportedOptions(): util.Set[SocketOption[_]] = ???

  override def implCloseSelectableChannel(): Unit = ???

  override def implConfigureBlocking(block: Boolean): Unit = ???
}

class TestSocketChannel(provider: SelectorProvider) extends SocketChannel(provider) {

  var readResult: Int = _
  var bytes: Array[Byte] = _
  var readCount = 0

  override def read(dst: ByteBuffer): Int = {
    dst.put(bytes)
    readCount += 1
    readResult
  }

  def reset(): Unit = {
    readCount = 0
    readResult = 0
    bytes = Array.emptyByteArray
  }

  override def bind(local: SocketAddress): SocketChannel = ???

  override def setOption[T](name: SocketOption[T], value: T): SocketChannel = ???

  override def shutdownInput(): SocketChannel = ???

  override def shutdownOutput(): SocketChannel = ???

  override def socket(): Socket = ???

  override def isConnected: Boolean = ???

  override def isConnectionPending: Boolean = ???

  override def connect(remote: SocketAddress): Boolean = ???

  override def finishConnect(): Boolean = ???

  override def getRemoteAddress: SocketAddress = ???

  override def read(dsts: Array[ByteBuffer], offset: Int, length: Int): Long = ???

  override def write(src: ByteBuffer): Int = ???

  override def write(srcs: Array[ByteBuffer], offset: Int, length: Int): Long = ???

  override def getLocalAddress: SocketAddress = ???

  override def getOption[T](name: SocketOption[T]): T = ???

  override def supportedOptions(): util.Set[SocketOption[_]] = ???

  override def implCloseSelectableChannel(): Unit = {}

  override def implConfigureBlocking(block: Boolean): Unit = ???
}