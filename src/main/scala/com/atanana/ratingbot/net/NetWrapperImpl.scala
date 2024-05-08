package com.atanana.ratingbot.net

import cats.effect.IO
import com.atanana.ratingbot.json.Config
import sttp.capabilities.WebSockets
import sttp.client3.*
import sttp.model.Uri

class NetWrapperImpl(config: Config, backend: SttpBackend[IO, WebSockets]) extends NetWrapper {

  private val authCookie = ("REMEMBERME", config.authCookie)

  override def getPageAsync(uri: Uri): IO[Either[String, String]] =
    basicRequest
      .get(uri)
      .cookie(authCookie)
      .send(backend)
      .map(_.body)

  override def postAsync(uri: Uri, params: Map[String, String]): IO[Either[String, String]] =
    basicRequest
      .body(params)
      .post(uri)
      .cookie(authCookie)
      .send(backend)
      .map(_.body)

  override def getApi(uri: Uri): IO[Either[String, String]] =
    basicRequest
      .get(uri)
      .send(backend)
      .map(_.body)
}
