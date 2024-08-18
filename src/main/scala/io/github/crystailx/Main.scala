package io.github.crystailx

import app.tulz.diff.StringDiff
import io.github.crystailx.config.CmdArgs
import scribe.format._
import scribe.{Level, Logger, Logging}

object Main extends App with Logging {
  val config = CmdArgs(args)

  Logger.root
    .clearHandlers()
    .withMinimumLevel(if (config.verbose()) Level.Debug else Level.Info)
    .withHandler(formatter = formatter"[$dateFull][$levelColored] $messages")
    .replace()

  val fhjClient = new FanhuajiClient()

  lazy val overwriteFileMsg = s"File '${config.output()}' already exists. Overwrite? [y/N]"
  lazy val writeFileMsg     = s"Writing conversion result to file '${config.output()}'"

  def showDiff(text: String, converted: String): Either[Exception, Unit] = Right {
    logger.debug(s"\n${StringDiff(text, converted, collapse = true)}")
  }

  def readAndConvert: Either[Throwable, String] = for {
    mode      <- config.mode()
    inputFile <- getExistFile(config.input()).toEither
    text      <- readWholeFile(inputFile).toEither
    converted <- fhjClient.convert(mode, text).map(_.data.text)
    _         <- showDiff(text, converted)
  } yield converted

  def writeResult(convertedText: String): Unit =
    getWritableFile(config.output(), overwriteFileMsg)
      .foreach { file =>
        logger.info(writeFileMsg)
        writeToFile(file, convertedText)
      }

  readAndConvert.map(writeResult).fold(throw _, identity)

}
