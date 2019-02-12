import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * Settings that are comment to all the SBT projects
  */
object Common extends AutoPlugin {
  override def trigger = allRequirements
  override def requires: sbt.Plugins = JvmPlugin

  override def projectSettings = Seq(
    organization := "com.github.liquidcore7",
    version := "0.1-PRERELEASE",
    resolvers += Resolver.typesafeRepo("releases"),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-target:jvm-1.8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Yno-adapted-args",
      "-Ywarn-numeric-widen",
      "-Xfatal-warnings"
    ),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    autoAPIMappings := true
  )
}
