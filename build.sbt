name := "rating-bot"

assemblyJarName in assembly := "rating-bot.jar"

version := "2.1.1"

scalaVersion := "2.12.7"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.3"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.1.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test