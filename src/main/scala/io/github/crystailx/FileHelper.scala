package io.github.crystailx

import io.github.crystailx.FileHelper.UnsupportedFileType

import java.io.{File, FileNotFoundException, FileOutputStream, OutputStreamWriter}
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

trait FileHelper {
  private val UTF8 = "UTF-8"

  def readWholeFile(file: File): Try[String] = Using(Source.fromFile(file, UTF8))(_.getLines().mkString("\n"))

  def writeToFile(file: File, text: String): Try[Unit] =
    Using(new OutputStreamWriter(new FileOutputStream(file, false), UTF8)) { writer =>
      writer.write(text)
      writer.flush()
    }

  def getExistFile(path: String): Try[File] = Try(new File(path)) match {
    case Success(value) if !value.exists()                 => Failure(new FileNotFoundException(path))
    case Success(value) if value.exists() && !value.isFile => Failure(UnsupportedFileType)
    case success @ Success(value) if value.exists()        => success
    case failure                                           => failure
  }

  def getWritableFile(path: String, overwriteCheckMsg: => String): Try[File] = Try(new File(path)) match {
    case Success(value) if !value.exists()                                            => Success(value)
    case Success(value) if value.exists() && !value.isFile                            => Failure(UnsupportedFileType)
    case Success(value) if value.exists() && checkOverwrite(value, overwriteCheckMsg) => Success(value)
    case failure                                                                      => failure
  }

  protected def checkOverwrite(file: File, msg: => String): Boolean =
    if (file.exists()) readLine(msg).toLowerCase == "y" else true
}
object FileHelper {
  case object UnsupportedFileType extends Exception
}
