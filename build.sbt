name := "rating-bot"

assembly / assemblyJarName := "rating-bot.jar"

version := "2.5.2"

scalaVersion := "2.13.8"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.5"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.0.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.0"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.10"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.10"
libraryDependencies += "com.softwaremill.sttp.client3" %% "okhttp-backend" % "3.8.10"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "5.2.0" % Test

assembly / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

scalacOptions += "-deprecation"