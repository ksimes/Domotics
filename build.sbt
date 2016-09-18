name := "Domotics "

version := "1.0"

scalaVersion := "2.10.3"

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
