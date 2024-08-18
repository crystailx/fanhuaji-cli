package io.github

import enumeratum.{CirceEnum, Enum, EnumEntry}

package object crystailx extends ConsoleReader with FileHelper {
  sealed abstract class Mode(val desc: String) extends EnumEntry
  object Mode extends Enum[Mode] with CirceEnum[Mode] {
    case object Simplified      extends Mode("簡體化")
    case object Traditional     extends Mode("繁體化")
    case object China           extends Mode("中國化")
    case object Hongkong        extends Mode("香港化")
    case object Taiwan          extends Mode("台灣化")
    case object Pinyin          extends Mode("拼音化")
    case object Bopomofo        extends Mode("注音化")
    case object Mars            extends Mode("火星化")
    case object WikiSimplified  extends Mode("維基簡體化")
    case object WikiTraditional extends Mode("維基繁體化")

    override def values: IndexedSeq[Mode] = findValues

    override def toString: String = values.map(m => s"${m.entryName}(${m.desc})").mkString(",")
  }
}
