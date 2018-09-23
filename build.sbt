import sbtassembly.MergeStrategy

enablePlugins(AwsLambdaPlugin)

name := "aws-lambda-piggybank"

organization := "net.cgbaker.lambdas"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.4.18"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-dynamodb" % "1.1.1",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.1.0",
  "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.403",
  "com.amazon.alexa" % "alexa-skills-kit" % "1.1",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.25",
  "org.slf4j" % "slf4j-log4j12" % "1.7.25",
  "commons-io" % "commons-io" % "2.6",
  "org.apache.commons" % "commons-lang3" % "3.1",
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0",
  //
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.4.18.2" // % "test"

)

val reverseConcat: MergeStrategy = new MergeStrategy {
  val name = "reverseConcat"
  def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] =
    MergeStrategy.concat(tempDir, path, files.reverse)
}

assemblyMergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x) => reverseConcat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

handlerName := Some("net.cgbaker.lambdas.piggybank.PiggyBankHandler")

lambdaName := Some("piggybank")

deployMethod := Some("S3")

region := Some("us-east-1")

