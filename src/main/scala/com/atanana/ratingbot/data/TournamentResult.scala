package com.atanana.ratingbot.data

import com.atanana.ratingbot.types.Ids.TournamentId

case class TournamentResult(id: TournamentId, questionsCount: Int, position: Float, bonus: Int)
