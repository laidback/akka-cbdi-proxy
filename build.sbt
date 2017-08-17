name := "generic-jerry"

version := "1.0"

maintainer := "Lukas Ciszewski <Lukas.Ciszewski@Gmail.com"

packageSummary := "Jerry internal API Service"

scalaVersion := "2.12.3"

scalacOptions += "-deprecation"

lazy val akkaHttpVersion = "10.0.9"
lazy val akkaVersion = "2.4.19"

enablePlugins(JavaServerAppPackaging, GitBranchPrompt)

fork in run := false

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe" % "config" % "1.3.1",
  "ch.qos.logback" % "logback-classic" % "1.2.1" % Runtime
)

mappings in Universal += {
  val conf = (resourceDirectory in Compile).value / "application.conf"
  conf -> "conf/application.conf"
}

mappings in Universal += {
  val log = (resourceDirectory in Compile).value / "logback.xml"
  log -> "conf/logback.xml"
}

// Native packager docker settings
packageName in Docker := name.value
version in Docker := version.value
maintainer in Docker := maintainer.value

// dockerBaseImage := "registry.gitlab01.fti.int/lcb/fti-akka-skeleton/scala-sbt-docker"

dockerRepository := Some("registry.gitlab01.fti.int/lcb")

dockerUpdateLatest := true
