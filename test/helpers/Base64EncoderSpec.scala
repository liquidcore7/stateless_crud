package helpers

import com.google.common.base.Charsets
import org.scalatest.{FlatSpec, Matchers}


class Base64EncoderSpec extends FlatSpec with Matchers {

  val testText: GzippedData = GzippedData(
    "Hello world,?!:^^їє$#__\"bye|world\"".getBytes(Charsets.UTF_8)
  )

  "Base64 encoder" should "hold inverse property" in {
    Base64Encoder.inverse( Base64Encoder.direct(testText) ).byteArray shouldEqual testText.byteArray
  }

}
