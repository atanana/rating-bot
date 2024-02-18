package com.atanana.data

import com.atanana.types.Ids.TournamentId

case class TournamentResult(id: TournamentId, questionsCount: Int, position: Float, bonus: Int)
