package com.atanana

import java.io.PrintWriter

import scala.io.Source
import scala.util.Try

class FsHandler {
  def readFile(filename: String): Try[String] = {
    Try({
      val source = Source.fromFile(filename)
      try {
        source.getLines().mkString
      } finally {
        source.close()
      }
    })
  }

  def writeFile(contents: String, filename: String): Unit = {
    val writer = new PrintWriter(filename)
    try {
      writer.print(contents)
    } finally {
      writer.close()
    }
  }
}
