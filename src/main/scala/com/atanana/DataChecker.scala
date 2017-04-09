package com.atanana

import com.atanana.data.{Data, Tournament, TournamentData}

class DataChecker(private val poster: Poster, private val store: JsonStore, private val messageComposer: MessageComposer) {
  def check(data: List[TournamentData]): Unit = {
    val storedData = store.read
    checkForNewResults(data, storedData)
    checkForChangedResults(data, storedData)
  }

  def checkForChangedResults(data: List[TournamentData], storedData: Data): Unit = {
    val newTournamentsData: Set[Tournament] = getTournamentsData(data)
    val changedResults: Set[Tournament] = storedData.tournaments -- newTournamentsData
    if (changedResults.nonEmpty) {
      store.write(storedData.copy(tournaments = newTournamentsData))
      changedResults.foreach(tournament => {
        val tournamentData: TournamentData = data.find(_.id == tournament.id).get
        val message: String = messageComposer.composeChangedResult(tournamentData, tournament.score)
        poster.post(message)
      })
    }
  }

  def checkForNewResults(data: List[TournamentData], storedData: Data): Unit = {
    val newTournamentIds: Set[Int] = data.map(_.id).toSet -- storedData.tournaments.map(_.id)
    if (newTournamentIds.nonEmpty) {
      val newTournamentsData: Set[Tournament] = getTournamentsData(data)
      store.write(storedData.copy(tournaments = newTournamentsData))
      newTournamentIds.foreach(id => {
        val tournamentData: TournamentData = data.find(_.id == id).get
        if (storedData.tournaments.nonEmpty) {
          val message: String = messageComposer.composeNewResult(tournamentData)
          poster.post(message)
        }
      })
    }
  }

  def getTournamentsData(data: List[TournamentData]): Set[Tournament] = {
    data.map(tournament => Tournament(tournament.id, tournament.questions)).toSet
  }
}

object DataChecker {
  def apply(poster: Poster, store: JsonStore, messageComposer: MessageComposer) = new DataChecker(poster, store, messageComposer)
}