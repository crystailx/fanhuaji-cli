package io.github.crystailx

import scala.io.StdIn

trait ConsoleReader {
  def readLine(msg: String): String = {
    Console.print(msg)
    StdIn.readLine()
  }
}
