package fun

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

object Globals {
  var count = 0
  def handler(in: InputStream, out: OutputStream): Unit = {
    count += 1
    out.write(formatCount().getBytes(UTF_8))
  }

  def formatCount(): String = {
    count match {
      case 1 =>
        "count is still zero :/"
      case _ =>
        s"count is $count :)"
    }
  }
}
