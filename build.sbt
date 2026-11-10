ThisBuild / scalaVersion := "3.10.0"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "zio-tutorial",
    libraryDependencies += "dev.zio" %% "zio" % "2.1.27"
  )
