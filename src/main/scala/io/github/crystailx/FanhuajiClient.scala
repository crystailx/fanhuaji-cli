package io.github.crystailx
import io.circe.generic.JsonCodec
import io.github.crystailx.FanhuajiClient._
import sttp.client3._
import sttp.client3.circe._
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
  case class ConvertBody(text: String, converter: Mode)
  @JsonCodec
  case class ConvertResult(text: String)
  @JsonCodec
  case class FHJResponse[T](data: T)
}
