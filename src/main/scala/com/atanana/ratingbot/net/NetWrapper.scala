package com.atanana.ratingbot.net

import cats.effect.IO
import sttp.model.Uri

trait NetWrapper {

  def getPageAsync(uri: Uri): IO[Either[String, String]]

  def postAsync(uri: Uri, params: Map[String, String]): IO[Either[String, String]]

  def getApi(uri: Uri): IO[Either[String, String]]
}
