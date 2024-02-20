package com.atanana.ratingbot.parsers

import com.atanana.ratingbot.data.Release

import scala.util.Try

trait ReleasesParser {

  def getReleases(releasesPage: String): Try[List[Release]]
}
