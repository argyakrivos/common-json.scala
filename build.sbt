lazy val root = (project in file(".")).
  settings(
    name := "common-json",
    organization := "com.blinkbox.books",
    version := scala.util.Try(scala.io.Source.fromFile("VERSION").mkString.trim).getOrElse("0.0.0"),
    scalaVersion := "2.11.4",
    crossScalaVersions := Seq("2.11.4"),
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8", "-target:jvm-1.7", "-Xfatal-warnings", "-Xfuture"),
    libraryDependencies ++= {
      val json4sV = "3.2.11"
      Seq(
        "joda-time"          %  "joda-time"         % "2.5",
        "org.joda"           %  "joda-convert"      % "1.7",
        "org.json4s"         %% "json4s-jackson"    % json4sV,
        "org.json4s"         %% "json4s-ext"        % json4sV,
        "com.blinkbox.books" %% "common-scala-test" % "0.3.0"  % Test
      )
    }
  )
