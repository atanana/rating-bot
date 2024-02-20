package com.atanana.ratingbot.net

import com.atanana.ratingbot.json.Config
import sttp.client3.*
import sttp.client3.okhttp.OkHttpFutureBackend
import sttp.model.{Header, Uri}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NetWrapperImpl(config: Config) extends NetWrapper {

  private val asyncBackend = OkHttpFutureBackend()
  private val authCookie = ("REMEMBERME", config.authCookie)

  override def getPageAsync(uri: Uri): Future[Either[String, String]] =
    basicRequest
      .get(uri)
      .cookie(authCookie)
      .send(asyncBackend)
      .map(_.body)

  override def postAsync(uri: Uri, params: Map[String, String]): Future[Either[String, String]] =
    basicRequest
      .body(params)
      .post(uri)
      .cookie(authCookie)
      .send(asyncBackend)
      .map(_.body)

  override def getApi(uri: Uri): Future[Either[String, String]] =
    basicRequest
      .get(uri)
      .send(asyncBackend)
      .map(_.body)
}
