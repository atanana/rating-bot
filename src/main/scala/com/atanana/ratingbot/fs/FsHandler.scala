package com.atanana.ratingbot.fs

import scala.util.Try

trait FsHandler {

  def readFile(filename: String): Try[String]

  def writeFile(contents: String, filename: String): Unit
}
