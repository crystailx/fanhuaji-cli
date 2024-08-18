package io.github.crystailx
package config

import enumeratum.NoSuchMember
import org.rogach.scallop._

private[config] class CmdArgs(arguments: Seq[String]) extends ScallopConf(arguments) {
  version(BuildInfo.version)
  banner("""
      |本程式使用了繁化姬的 API 服務
      |若將繁化姬使用於商用必須付費
      |https://zhconvert.org/
      |""".stripMargin)
  val input: ScallopOption[String] = opt[String](short = 'i', required = true)
  val mode: ScallopOption[Either[NoSuchMember[Mode], Mode]] = choice(
    Mode.values.map(_.entryName),
    short = 'm',
    descr = s"所要使用的轉換器:$Mode"
  ).map(Mode.withNameInsensitiveEither)
  val verbose: ScallopOption[Boolean] = toggle(default = Some(false))
  val output: ScallopOption[String] = trailArg[String]()
  verify()
}
object CmdArgs {
  def apply(arguments: Seq[String]): CmdArgs = new CmdArgs(arguments)
}
