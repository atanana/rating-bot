name := "rating-bot"

version := "3.3.3"

scalaVersion := "3.6.3"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.16"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.1.2"
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.6.5"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.10.2"
libraryDependencies += "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.10.2"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.13.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"

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
