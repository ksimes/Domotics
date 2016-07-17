name := "Domotics"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-web" % "1.3.5.RELEASE",
  "com.darwinsys" % "hirondelle-date4j" % "1.5.1",
  "mysql" % "mysql-connector-java" % "5.1.39",
  "com.jayway.jsonpath" % "json-path-assert" % "0.8.1" % "test",
  "org.springframework" % "spring-test" % "2.5" % "test",
  "junit" % "junit" % "4.11" % "test"
)
