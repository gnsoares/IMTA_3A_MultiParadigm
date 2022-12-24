val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Scalix",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )

libraryDependencies += "org.json4s" %% "json4s-ast" % "4.0.6"
libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.6"
