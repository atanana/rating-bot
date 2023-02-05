package com.atanana.net

import com.atanana.json.Config
import sttp.client3.*
import sttp.client3.okhttp.OkHttpFutureBackend
import sttp.model.{Header, Uri}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NetWrapperImpl(config: Config) extends NetWrapper {

  private val asyncBackend = OkHttpFutureBackend()
  private val authCookie = ("REMEMBERME", config.authCookie)
  private val apiAuthHeader = Header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOjE2NjQ3MTgyODAsImV4cCI6MTY2NDcyMTg4MCwicm9sZXMiOltdLCJ1c2VybmFtZSI6InRhbmFuYS5hbmRyZXlAZ21haWwuY29tIn0.RkhxOTysbw5Hn3rysEU2kRUrnM2DuPkmQkTDzWiQNqA6ehYFFtCLyI6GX-7kwSuHvhiLnYdiv6IREw9QwPxO3wsT4tjNALG05NWpxNLnJQjLt85Y5aaQ1f3vc5QZ7kfCkJiNlPSz-YES_03kCdmym1rTLqM1K2jBPbV7qlRbBRefH8c1aXE7mhx9qZHXVgXbTYcA8-9wGyZ_4VZa8cwLXOhYBmZtQedoBrCP6rk8LEC1VgyJD4hWd2x6oOsajf-tHK3F8zi6UuxG7LLejQ-eAo_EUi8F0UOIRXnUiy4f6JYkHoX8szNUaCoxFlT6qWVg1Yd7WQgD3NN3KVM8f0li0A")

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
      .header(apiAuthHeader)
      .send(asyncBackend)
      .map(_.body)
}
