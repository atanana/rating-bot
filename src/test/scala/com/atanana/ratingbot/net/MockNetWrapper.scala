package com.atanana.ratingbot.net

import cats.effect.IO
import com.atanana.ratingbot.net.NetWrapper
import sttp.model.Uri

import scala.collection.mutable

class MockNetWrapper extends NetWrapper {

  val pageResponses: mutable.Map[Uri, IO[Either[String, String]]] = mutable.Map[Uri, IO[Either[String, String]]]()
  val postResponses: mutable.Map[(Uri, Map[String, String]), IO[Either[String, String]]] = mutable.Map[(Uri, Map[String, String]), IO[Either[String, String]]]()
  val apiResponses: mutable.Map[Uri, IO[Either[String, String]]] = mutable.Map[Uri, IO[Either[String, String]]]()

  override def getPageAsync(uri: Uri): IO[Either[String, String]] = pageResponses(uri)

  override def postAsync(uri: Uri, params: Map[String, String]): IO[Either[String, String]] =
    postResponses((uri, params))

  override def getApi(uri: Uri): IO[Either[String, String]] = apiResponses(uri)

  def clear(): Unit = {
    pageResponses.clear()
    postResponses.clear()
    apiResponses.clear()
  }
}
