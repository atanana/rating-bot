package com.atanana.mocks

import com.atanana.data.Release
import com.atanana.parsers.ReleasesParser

import scala.collection.mutable
import scala.util.Try

class MockReleasesParser extends ReleasesParser {

  val releases: mutable.Map[String, Try[List[Release]]] = mutable.Map()

  override def getReleases(releasesPage: String): Try[List[Release]] = releases(releasesPage)
}
