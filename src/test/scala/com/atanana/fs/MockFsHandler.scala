package com.atanana.fs

import java.lang
import scala.collection.mutable
import scala.util.Try

class MockFsHandler extends FsHandler {

  private val files = mutable.Map[String, String]()

  override def readFile(filename: String): Try[String] = files.get(filename)
    .toRight(new RuntimeException(s"No file $filename"))
    .toTry

  override def writeFile(contents: String, filename: String): Unit = files.put(filename, contents)

  def clear(): Unit = files.clear()
}
