package com.atanana.ratingbot.types

import scala.math.Ordering

object Ids {

  opaque type TournamentId = Int

  object TournamentId {

    def apply(int: Int): TournamentId = int

    val ordering: Ordering[TournamentId] = Ordering.Int

    extension (id: TournamentId) def value: Int = id
  }

  opaque type ReleaseId = Int

  object ReleaseId {

    def apply(int: Int): ReleaseId = int
  }

  opaque type TeamId = Int

  object TeamId {

    def apply(int: Int): TeamId = int

    extension (id: TeamId) def value: Int = id
  }
}
