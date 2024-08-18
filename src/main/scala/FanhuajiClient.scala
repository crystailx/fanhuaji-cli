package io.github.crystailx
import sttp.client3._
import sttp.client3.circe._
import FanhuajiClient._

import io.circe.generic.JsonCodec
class FanhuajiClient(host: String = "https://api.zhconvert.org") {
  private val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()

  def convert(mode: Mode, text: String): Either[Exception, FHJResponse[ConvertResult]] = basicRequest
    .post(uri"$host/convert")
    .body(ConvertBody(text, mode))
    .response(asJson[FHJResponse[ConvertResult]])
    .send(backend)
    .body

}
object FanhuajiClient {
  @JsonCodec
  case class ConvertBody(
      text: String,
      converter: Mode
  )
  @JsonCodec
  case class Revisions(build: String, msg: String, time: Long)
  @JsonCodec
  case class ConvertResult(
      converter: Mode,
      text: String,
      diff: Option[String],
      usedModules: List[String],
      jpTextStyles: List[String],
      textFormat: String
  )
  @JsonCodec
  case class FHJResponse[T](
      code: Int,
      msg: String,
      execTime: BigDecimal,
      revisions: Revisions,
      data: T
  )
}
