package helpers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.Charset
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import com.google.common.base.Charsets

import scala.io.Codec
import scala.language.reflectiveCalls

object GzipConverter extends BidirectionalConverter[String, String] {

  private val utf8: Charset = Charsets.UTF_8

  override def direct(s: String): String = {
    type Streams = (ByteArrayOutputStream, GZIPOutputStream)

    val writeStreams = { byteStream: ByteArrayOutputStream =>
      val gzipStream = new GZIPOutputStream(byteStream)
      gzipStream.write(s.getBytes(utf8))

      byteStream -> gzipStream -> byteStream.toByteArray
    }

    writeStreams.andThen { case (streams: Streams, compressed: Array[Byte]) =>
      closeAll(streams._1, streams._2)
      new String(compressed, utf8)
    }.apply(new ByteArrayOutputStream(s.length))
  }

  private def closeAll(closeables: {def close(): Unit}*): Unit = {
    closeables.foreach(_.close())
  }

  override def inverse(a: String): String = {
    val inputStream = new GZIPInputStream(new ByteArrayInputStream(a.getBytes(utf8)))
    scala.io.Source.fromInputStream(inputStream)(Codec.UTF8).mkString
  }
}
