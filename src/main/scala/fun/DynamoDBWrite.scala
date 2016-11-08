package fun

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

import cats.data.Xor
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}

import scala.util.Try

object DynamoDBWrite {
  def handler(in: InputStream, out: OutputStream): Unit = {
    val inputString = scala.io.Source.fromInputStream(in).mkString
    println("input "+inputString.replaceAll("\n", ""))
    readInput(inputString)
      .flatMap(i => Try(write(i))
        .toXor
        .leftMap { ex =>
          println("err "+ex)
          ex.getMessage
        })
      .fold[Unit](
      bad => {
        val msg = "err " + bad + "\n input = "+inputString
        println(msg)
        out.write(msg.getBytes(UTF_8))
      },
      ok => {
        val msg = "ok "+ok
        println(ok)
        out.write(msg.getBytes(UTF_8))
      }
    )
  }

  def write(q: QueryParams): Unit = {
    val client = new AmazonDynamoDBClient()
    client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2))

    val db = new DynamoDB(client)
    val table = db.getTable("TestTable")

    val result = table.putItem(new Item()
      .withPrimaryKey("id", q.id)
      .withString("value", q.value)
    )
  }

  case class QueryParams(id: String, value: String)

  case class InputParams(querystring: QueryParams)

  case class Input(params: InputParams)

  def readInput(in: String): Xor[String, QueryParams] = {
    import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

    decode[Input](in).map(_.params.querystring).leftMap(_.getMessage)
  }

  implicit class TryOpts[T](t: Try[T]) {
    def toXor: Xor[Throwable, T] = t match {
      case util.Success(ok) =>
        Xor.Right(ok)
      case util.Failure(er) =>
        Xor.Left(er)
    }
  }
}
