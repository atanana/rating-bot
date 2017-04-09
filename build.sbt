name := "rating-bot"

version := "1.3"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.3"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "1.2.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test