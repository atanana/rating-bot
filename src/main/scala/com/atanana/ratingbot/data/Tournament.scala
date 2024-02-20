package com.atanana.ratingbot.data

import com.atanana.ratingbot.types.Ids.TournamentId

import scala.language.implicitConversions

case class Tournament(id: TournamentId, score: Int)
