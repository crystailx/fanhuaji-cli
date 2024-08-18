ThisBuild / version := "0.1.0"
ThisBuild / organization := "io.github.crystailx"
ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "fanhuaji-cli",
    idePackagePrefix := Some("io.github.crystailx"),
    assembly / mainClass := Some("io.github.crystailx.Main"),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "io.github.crystailx"
  )
  .enablePlugins(GraalVMNativeImagePlugin, BuildInfoPlugin)

ThisBuild / scalacOptions ++= Seq("-Ymacro-annotations")
libraryDependencies += "org.rogach" %% "scallop" % "5.1.0"
libraryDependencies += "io.circe" %% "circe-yaml-v12" % "1.15.0"
libraryDependencies += "io.circe" %% "circe-core" % "0.14.9"
libraryDependencies += "io.circe" %% "circe-parser" % "0.14.9"
libraryDependencies += "io.circe" %% "circe-generic-extras" % "0.14.3"
libraryDependencies ++= Seq(
  "com.beachape" %% "enumeratum" % "1.7.4",
  "com.beachape" %% "enumeratum-circe" % "1.7.4"
)
libraryDependencies += "com.outr" %% "scribe" % "3.15.0"
libraryDependencies += "com.outr" %% "scribe-slf4j" % "3.15.0"
libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core" % "3.9.7",
  "com.softwaremill.sttp.client3" %% "circe" % "3.9.7"
).map(_ exclude ("io.circe", "*"))

libraryDependencies += "app.tulz" %% "stringdiff" % "0.3.4"

ThisBuild / mainClass := Some("io.github.crystailx.Main")
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

graalVMNativeImageOptions ++= Seq(
  "--enable-url-protocols=http",
  "--enable-https"
)

graalVMNativeImageOptions ++= Seq(
  "--no-fallback",
  "-march=compatibility",
  "-Ob"
)

graalVMNativeImageOptions ++= Seq(
  "-H:JNIConfigurationFiles=../../src/graal/jni-config.json",
  "-H:ResourceConfigurationFiles=../../src/graal/resource-config.json",
  "-H:ReflectionConfigurationFiles=../../src/graal/reflect-config.json",
  "-H:DynamicProxyConfigurationFiles=../../src/graal/proxy-config.json"
)
