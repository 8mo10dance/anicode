lazy val root = (project in file(".")).settings(
  name := "anicode",
  version := "2.4.1-Unicorn",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.5",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )
)
