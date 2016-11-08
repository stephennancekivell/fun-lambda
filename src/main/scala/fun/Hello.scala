package fun

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

object Hello {
	def handler(in: InputStream, out: OutputStream): Unit = {
    val msg = "Hello world! v2"
    println(msg)
    val input = scala.io.Source.fromInputStream(in).mkString.replaceAll("\n", "")
    println("input "+input)
    out.write(msg.getBytes(UTF_8))
	}
}