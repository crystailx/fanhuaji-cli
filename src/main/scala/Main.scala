package io.github.crystailx

import config.CmdArgs

import app.tulz.diff.StringDiff
import scribe.format._

import java.io.{File, FileOutputStream, OutputStreamWriter}
import scala.io.{Source, StdIn}
import scala.util.Using

object Main extends App {
  val config = CmdArgs(args)
  scribe.Logger.root
    .clearHandlers()
    .withHandler(formatter = formatter"[$dateFull][$levelColored] $messages")
    .replace()

  val fhjClient = new FanhuajiClient()
  val inputText = Using(Source.fromFile(config.input()))(_.getLines().mkString("\n"))
  def showDiff(text: String, converted: String): Either[Exception, Unit] = Right {
    if (config.verbose()) println(StringDiff(text, converted, collapse = true))
  }
  val convertedText = (for {
    mode <- config.mode()
    text <- inputText.toEither
    converted <- fhjClient.convert(mode, text).map(_.data.text)
    _ <- showDiff(text, converted)
  } yield converted) fold (throw _, identity)
  val outputFile = new File(config.output())
  val write = if (outputFile.exists()) {
    Console.print(s"File '${config.output()}' already exists. Overwrite? [y/N]")
    StdIn.readLine().toLowerCase == "y"
  } else false
  if (write) {
    scribe.info(s"Writing conversion result to file '${config.output()}'")
    Using(new OutputStreamWriter(new FileOutputStream(outputFile, false), "UTF-8")) { writer =>
      writer.write(convertedText)
      writer.flush()
    }.fold(throw _, identity)
  }
}
