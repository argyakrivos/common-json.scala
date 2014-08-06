name := "common-json"

organization := "com.blinkbox.books"

version := scala.util.Try(scala.io.Source.fromFile("VERSION").mkString.trim).getOrElse("0.0.0")

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8", "-target:jvm-1.7")

libraryDependencies ++= {
  val json4sV = "3.2.10"
  Seq(
    "joda-time"           %   "joda-time"         % "2.3",
    "org.joda"            %   "joda-convert"      % "1.6",
    "org.json4s"          %%  "json4s-jackson"    % json4sV,
    "org.json4s"          %%  "json4s-ext"        % json4sV,
    "com.blinkbox.books"  %%  "common-scala-test" % "0.2.0" % "test"
  )
}
