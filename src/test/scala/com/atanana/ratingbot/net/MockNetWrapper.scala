package com.atanana.ratingbot.net

import com.atanana.ratingbot.net.NetWrapper
import sttp.model.Uri

import scala.collection.mutable
import scala.concurrent.Future

class MockNetWrapper extends NetWrapper {

  val pageResponses: mutable.Map[Uri, Future[Either[String, String]]] = mutable.Map[Uri, Future[Either[String, String]]]()
  val postResponses: mutable.Map[(Uri, Map[String, String]), Future[Either[String, String]]] = mutable.Map[(Uri, Map[String, String]), Future[Either[String, String]]]()
  val apiResponses: mutable.Map[Uri, Future[Either[String, String]]] = mutable.Map[Uri, Future[Either[String, String]]]()

  override def getPageAsync(uri: Uri): Future[Either[String, String]] = pageResponses(uri)

  override def postAsync(uri: Uri, params: Map[String, String]): Future[Either[String, String]] =
    postResponses((uri, params))

  override def getApi(uri: Uri): Future[Either[String, String]] = apiResponses(uri)

  def clear(): Unit = {
    pageResponses.clear()
    postResponses.clear()
    apiResponses.clear()
  }
}
