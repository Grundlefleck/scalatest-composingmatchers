name := "scalatest-composingmatchers"

organization := "com.github.grundlefleck"

version := "0.1"

scalaVersion := "2.9.3"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0.M5b" % "test"
)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                  Resolver.sonatypeRepo("snapshots"))
