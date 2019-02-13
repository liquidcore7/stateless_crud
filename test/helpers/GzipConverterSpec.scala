package helpers

import org.scalatest.{FlatSpec, Matchers}


class GzipConverterSpec extends FlatSpec with Matchers {

  val testText: String = "Hello world,?!:^^їє$#__\"bye|world\""

  "Gzip converter" should "compress and decompress strings" in {
    GzipConverter.inverse( GzipConverter.direct(testText) ) shouldEqual testText
  }

  it should "compose with base64 encoder" in {
    val composed = GzipConverter.combine(Base64Encoder)
    val compressedAndEncoded = composed.direct(testText)

    compressedAndEncoded.matches("^[a-zA-Z0-9/+]+[=]+$") shouldBe true
    composed.inverse(compressedAndEncoded) shouldEqual testText
  }

}
