import sbt._
object Dependencies {
  val circeVersion        = "0.14.9"
  val circeGenericVersion = "0.14.3"
  val scallopVersion      = "5.1.0"
  val enumeratumVersion   = "1.7.4"
  val scribeVersion       = "3.15.0"
  val sttpVersion         = "3.9.7"
  val stringdiffVersion   = "0.3.4"
  def circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core"           % circeVersion,
    "io.circe" %% "circe-parser"         % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeGenericVersion
  )

  def config: Seq[ModuleID] = Seq(
    "org.rogach" %% "scallop" % scallopVersion
  )

  def enum: Seq[ModuleID] = Seq(
    "com.beachape" %% "enumeratum"       % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion
  )

  def log: Seq[ModuleID] = Seq(
    "com.outr" %% "scribe"       % scribeVersion,
    "com.outr" %% "scribe-slf4j" % scribeVersion
  )
  def httpClient: Seq[ModuleID] = Seq(
    "com.softwaremill.sttp.client3" %% "core"  % sttpVersion,
    "com.softwaremill.sttp.client3" %% "circe" % sttpVersion
  ).map(_ exclude ("io.circe", "*"))

  def text: Seq[ModuleID] = Seq(
    "app.tulz" %% "stringdiff" % stringdiffVersion
  )
}
