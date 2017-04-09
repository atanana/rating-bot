package com.atanana

import java.util.concurrent.TimeUnit

import com.atanana.data.TournamentData
import com.atanana.parsers.CsvParser
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scalaj.http._

object Main extends App {
  override def main(args: Array[String]): Unit = Configurator(new SystemWrapper).config match {
    case Success(config) =>
      val connector: Connector = Connector(config)
      val parser: CsvParser = CsvParser()
      val dataChecker: DataChecker = DataChecker(Poster(connector, config), JsonStore(new FsHandler), MessageComposer())
      val logger = Logger("main")

      while (true) {
        try {
          val response: HttpResponse[String] = connector.getTeamPage
          val data: List[TournamentData] = parser.getTournamentsData(response.body)
          dataChecker.check(data)
        } catch {
          case e: Throwable => logger.debug("Error occurred!", e)
        }

        Thread.sleep(Duration(10, TimeUnit.MINUTES).toMillis)
      }
    case Failure(e) => println(e.getMessage)
  }
}
