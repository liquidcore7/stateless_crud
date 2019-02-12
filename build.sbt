import sbt.Keys._

scalaVersion in ThisBuild := "2.12.7"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

// The Play project itself
lazy val root = (project in file("."))
  .enablePlugins(Common, PlayScala)
  .settings(
    name := "stateless-crud",
  )
