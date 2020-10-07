val commonSettings = Seq(
  scalaVersion := "2.13.3",
  version := "0.1.0"
)

lazy val scalaLdapCore = (project in file("scala-ldap-core"))
  .settings(
    commonSettings,
    name := "scala-ldap-core",
    libraryDependencies ++= Seq(
      "org.scodec" %% "scodec-core" % "1.11.7",
      "org.scodec" %% "scodec-bits" % "1.1.20",
      "org.scodec" %% "scodec-core-testkit" % "1.11.4" % Test,
      "org.scalactic" %% "scalactic" % "3.2.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.0" % Test
    )
  )

lazy val scalaLdapClient = (project in file("scala-ldap-client"))
  .settings(
    commonSettings,
    name := "scala-ldap-client",
    libraryDependencies ++= Seq(
      "com.spinoco" %% "protocol-ldap" % "0.4.0-M1",
      "org.typelevel" %% "cats-core" % "2.0.0",
      "co.fs2" %% "fs2-core" % "2.4.4",
      "co.fs2" %% "fs2-io" % "2.4.4",
      "org.scodec" %% "scodec-stream" % "2.0.0",
      "org.scalactic" %% "scalactic" % "3.2.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.0" % Test
    )
  )