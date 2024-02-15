package com.atanana.types

object Ids {

  opaque type TournamentId = Int

  object TournamentId {

    def apply(int: Int): TournamentId = int

    extension (id: TournamentId) def toString: String = id.toString
  }
}
