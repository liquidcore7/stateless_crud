package helpers

import java.util.Base64

object Base64Encoder extends BidirectionalConverter[GzippedData, String] {

  val (encoder, decoder) = Base64.getEncoder -> Base64.getDecoder

  override def direct(gzip: GzippedData): String = encoder.encodeToString(gzip.byteArray)
  override def inverse(base64: String): GzippedData = GzippedData(decoder.decode(base64))
}
