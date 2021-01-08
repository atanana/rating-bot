name := "rating-bot"

assemblyJarName in assembly := "rating-bot.jar"

version := "2.3.0"

scalaVersion := "2.13.4"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.9"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.0.0-RC11"
libraryDependencies += "com.softwaremill.sttp.client3" %% "httpclient-backend" % "3.0.0-RC11"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.4.0" % Test