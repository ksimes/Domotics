import sbt.Keys._

lazy val commonSettings = Seq(
  organization := "stronans.com",
  version := "1.0.0",
  scalaVersion := "2.10.3",
  exportJars := true,
  // This forbids including Scala related libraries into the dependency
  autoScalaLibrary := false,
  // Enables publishing to maven repo
  publishMavenStyle := true,
  // Do not append Scala versions to the generated artifacts
  crossPaths := false
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Domotics"
  )

libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-web" % "1.4.0.RELEASE",
  "org.springframework.boot" % "spring-boot-configuration-processor" % "1.4.0.RELEASE",
  "org.springframework.boot" % "spring-boot-starter-actuator" % "1.4.0.RELEASE",
  "org.springframework" % "spring-test" % "2.5" % "test",
  "com.darwinsys" % "hirondelle-date4j" % "1.5.1",
  "mysql" % "mysql-connector-java" % "5.1.39",
  "com.jayway.jsonpath" % "json-path-assert" % "0.8.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

mainClass in assembly := Some("com.stronans.domotics.Application")

assemblyMergeStrategy in assembly := {
  case "application.yaml"	=> MergeStrategy.concat
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}