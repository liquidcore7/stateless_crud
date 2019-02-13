package helpers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.Charset
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import com.google.common.base.Charsets

import scala.io.{Codec, Source}


case class GzippedData(byteArray: Array[Byte])


object GzipConverter extends BidirectionalConverter[String, GzippedData] {

  private val utf8: Charset = Charsets.UTF_8

  override def direct(s: String): GzippedData = {

    val writeStreams = { byteStream: ByteArrayOutputStream =>
      val gzipStream = new GZIPOutputStream(byteStream)
      gzipStream.write(s.getBytes(utf8))
      gzipStream.close()

      byteStream -> byteStream.toByteArray
    }

    writeStreams.andThen { case (stream, byteArray) =>
      stream.close()
      GzippedData(byteArray)
    }.apply(new ByteArrayOutputStream(s.length))
  }

  override def inverse(a: GzippedData): String = {

    val readStreams = { inputStream: ByteArrayInputStream =>
      val gzipStream = new GZIPInputStream(inputStream)
      val result: String = Source.fromInputStream(gzipStream)(Codec.UTF8).mkString
      gzipStream.close()

      inputStream -> result
    }

    readStreams.andThen { case (stream, result) =>
        stream.close()
        result
    }.apply(new ByteArrayInputStream(a.byteArray))
  }

}
