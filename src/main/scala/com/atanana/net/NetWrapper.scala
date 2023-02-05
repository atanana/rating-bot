package com.atanana.net

import sttp.model.Uri

import scala.concurrent.Future

trait NetWrapper {

  def getPageAsync(uri: Uri): Future[Either[String, String]]

  def postAsync(uri: Uri, params: Map[String, String]): Future[Either[String, String]]

  def getApi(uri: Uri): Future[Either[String, String]]
}
