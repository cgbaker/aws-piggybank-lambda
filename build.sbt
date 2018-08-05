name := "aws-lambda-piggybank"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.14"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-dynamodb" % "1.1.1",
  "com.amazonaws" % "aws-lambda-java-core" % "1.0.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.0.0",
  //
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.1.1" % "test"

)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
