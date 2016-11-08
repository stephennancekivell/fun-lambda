scalaVersion := "2.11.8"

name := "fun-lambda"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.24"
)

val circeVersion = "0.4.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

enablePlugins(AwsLambdaPlugin)

lambdaHandlers := Seq(
  "hello" -> "fun.Hello::hello"
  //"count" -> "fun.Globals::handler",
  //"queryParams" -> "fun.QueryParams::handler",
  //"dynamoDbWrite" -> "fun.DynamoDBWrite::handler"
)


roleArn := Some("arn:aws:iam::306621356636:role/lambda_basic_execution")

s3Bucket := Some("lambda.stephenn.com")

region := Some("ap-southeast-2")