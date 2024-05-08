package com.atanana.ratingbot

import cats.data.EitherT
import cats.effect.IO
import com.typesafe.scalalogging.Logger

import java.io.{BufferedReader, InputStreamReader}
import java.net.ServerSocket
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

class CommandProvider(serverSocket: ServerSocket) {

  private val logger = Logger("CommandProvider")

  def getCommand: EitherT[IO, Throwable, String] =
    EitherT {
      IO.blocking {
        try {
          val socket = serverSocket.accept()
          val reader = new BufferedReader(new InputStreamReader(socket.getInputStream))
          Right(reader.readLine())
        }
        catch {
          case NonFatal(e) =>
            logger.error("Error in socket!", e)
            Left(e)
        }
      }
    }
}
