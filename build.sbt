ThisBuild / version      := "0.1.0"
ThisBuild / organization := "io.github.crystailx"
ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name                 := "fanhuaji-cli",
    assembly / mainClass := Some(s"${organization.value}.Main"),
    buildInfoKeys        := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage     := organization.value
  )
  .enablePlugins(GraalVMNativeImagePlugin, BuildInfoPlugin)

ThisBuild / scalacOptions ++= Seq("-Ymacro-annotations")

libraryDependencies ++= Dependencies.circe ++ Dependencies.config ++ Dependencies.enum ++
  Dependencies.log ++ Dependencies.httpClient ++ Dependencies.text

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
//  "-H:-EnableSecurityServicesFeature",
  "-H:+TraceSecurityServices",
  "-H:+ReportExceptionStackTraces",
  "-H:JNIConfigurationFiles=../../src/graal/jni-config.json",
  "-H:ResourceConfigurationFiles=../../src/graal/resource-config.json",
  "-H:ReflectionConfigurationFiles=../../src/graal/reflect-config.json",
  "-H:DynamicProxyConfigurationFiles=../../src/graal/proxy-config.json"
)
