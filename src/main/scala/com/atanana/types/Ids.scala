package com.atanana.types

object Ids {

  opaque type TournamentId = Int

  object TournamentId {

    def apply(int: Int): TournamentId = int

    extension (id: TournamentId) def toString: String = id.toString
  }

  opaque type ReleaseId = Int

  object ReleaseId {

    def apply(int: Int): ReleaseId = int

    extension (id: ReleaseId) def toString: String = id.toString
  }

  opaque type TeamId = Int

  object TeamId {

    def apply(int: Int): TeamId = int

    extension (id: TeamId) def value: Int = id

    extension (id: TeamId) def toString: String = id.toString
  }
}
