name := "rating-bot"

assembly / assemblyJarName := "rating-bot.jar"

version := "2.3.5"

scalaVersion := "2.13.8"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.0.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.0"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.10"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.6.2"
libraryDependencies += "com.softwaremill.sttp.client3" %% "okhttp-backend" % "3.6.2"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "5.2.0" % Test