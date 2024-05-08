name := "rating-bot"

assembly / assemblyJarName := "rating-bot.jar"

version := "3.1.2"

scalaVersion := "3.3.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.14"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.1.0"
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.5.9"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.9.5"
libraryDependencies += "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.9.5"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.4"

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