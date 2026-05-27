name := "rating-bot"

version := "3.4.1"

scalaVersion := "3.7.4"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.32"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.6"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.2.0"
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.6.7"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.11.0"
libraryDependencies += "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.11.0"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.13.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.7.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test"

assembly / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/io.netty.versions.properties") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}
assembly / assemblyOutputPath := file("target/rating-bot.jar")

scalacOptions += "-rewrite"
scalacOptions += "-new-syntax"
scalacOptions += "-explain"
