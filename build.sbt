name := "rating-bot"

assemblyJarName in assembly := "rating-bot.jar"

version := "2.3.3"

scalaVersion := "2.13.8"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.1"
libraryDependencies += "net.codingwell" %% "scala-guice" % "5.0.2"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.10"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.6.1"
libraryDependencies += "com.softwaremill.sttp.client3" %% "okhttp-backend" % "3.6.1"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "5.2.0" % Test