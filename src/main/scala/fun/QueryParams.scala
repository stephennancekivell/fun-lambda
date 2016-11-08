package fun

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets._
import java.time.LocalDateTime

object QueryParams {
  def handler(in: InputStream, out: OutputStream): Unit = {
    val input = scala.io.Source.fromInputStream(in).mkString
    println(LocalDateTime.now().toString+ " input "+input)
    out.write(input.getBytes(UTF_8))
  }
}
