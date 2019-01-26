lazy val root = (project in file(".")).settings(
  name := "anicode v2.4.0",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.5",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )
)
