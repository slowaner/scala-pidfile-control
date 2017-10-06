import sbt.Keys._
import sbt._
import xerial.sbt.pack.PackPlugin

val organizationName = "com.github.slowaner.scala"
val rootProjectName = "pidfile-control"

// Versions
// Testing
val scalatestVersion = "3.0.3"
val junitVersion = "4.12"

lazy val commonSettings = Defaults.defaultConfigs ++ Seq(
  organization := organizationName,
  scalaVersion := "2.12.3",
  crossPaths := false,
  libraryDependencies ++= Seq(
    // Testing
    "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    "junit" % "junit" % junitVersion % Test
  ),
  excludeFilter := new SimpleFileFilter(f => f.getName match {
    case ".gitignore" | ".gitkeep" => true
    case _ => false
  })
)

lazy val rootSettings = PackPlugin.packSettings ++ Seq(
  name := rootProjectName
)

lazy val root = Project(rootProjectName, file("."))
  .settings(commonSettings ++ rootSettings)
  .enablePlugins(PackPlugin)
