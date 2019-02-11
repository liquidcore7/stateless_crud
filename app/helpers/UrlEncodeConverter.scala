package helpers

import java.net.{URLDecoder, URLEncoder}

object UrlEncodeConverter extends BidirectionalConverter[String, String] {
  private val encoding: String = "UTF-8"

  override def direct(plain: String): String = URLEncoder.encode(plain, encoding)
  override def inverse(encoded: String): String = URLDecoder.decode(encoded, encoding)
}
