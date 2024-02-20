package com.atanana.ratingbot.fs

import java.io.PrintWriter
import scala.io.Source
import scala.util.{Try, Using}

class FsHandlerImpl extends FsHandler {
  override def readFile(filename: String): Try[String] =
    Using(Source.fromFile(filename)) { source =>
      source.getLines().mkString
    }

  override def writeFile(contents: String, filename: String): Unit = {
    Using(new PrintWriter(filename)) { writer =>
      writer.print(contents)
    }
  }
}
