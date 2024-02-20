package com.atanana.ratingbot.mocks

import com.atanana.ratingbot.data.Release
import com.atanana.ratingbot.parsers.ReleasesParser

import scala.collection.mutable
import scala.util.Try

class MockReleasesParser extends ReleasesParser {

  val releases: mutable.Map[String, Try[List[Release]]] = mutable.Map()

  override def getReleases(releasesPage: String): Try[List[Release]] = releases(releasesPage)
}
