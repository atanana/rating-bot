name := "rating-bot"

assembly / assemblyJarName := "rating-bot.jar"

version := "3.0.3"

scalaVersion := "3.3.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.14"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.0.0"
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.5.8"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.13"
libraryDependencies += "com.softwaremill.sttp.client3" %% "okhttp-backend" % "3.8.13"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"

assembly / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

scalacOptions += "-rewrite"
scalacOptions += "-new-syntax"
scalacOptions += "-explain"