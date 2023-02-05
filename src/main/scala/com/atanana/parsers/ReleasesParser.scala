package com.atanana.parsers

import com.atanana.data.Release

import scala.util.Try

trait ReleasesParser {

  def getReleases(releasesPage: String): Try[List[Release]]
}
